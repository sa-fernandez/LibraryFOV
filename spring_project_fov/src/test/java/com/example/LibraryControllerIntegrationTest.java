package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.example.dto.DTOLink;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.RealBook;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import com.example.repository.LoanRepository;
import com.example.repository.RealBookRepository;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LibraryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    RealBookRepository realBookRepository;

    @Autowired
    TestRestTemplate rest;

    @BeforeEach
    void init() {
        Author author1 = new Author("Migue");
        Author author2 = new Author("Kike");
        Author author3 = new Author("Asder");
        Book book1 = new Book("8273", "Guía práctica para Spring");
        Book book2 = new Book("3421", "Guía práctica para Angular");
        Book book3 = new Book("9790", "Guía práctica para Testing");
        RealBook realBook1 = new RealBook("BUENO", "10/10/2022");
        RealBook realBook2 = new RealBook("REGULAR", "11/10/2022");
        RealBook realBook3 = new RealBook("MALO", "12/10/2022");
        book1.getAuthors().add(author1);
        book2.getAuthors().add(author2);
        book3.getAuthors().add(author3);
        book1.getCopies().add(realBook1);
        book2.getCopies().add(realBook2);
        book3.getCopies().add(realBook3);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    @Test
    void testBuscarLibro() throws Exception {
        Book book = rest.getForObject("http://localhost:" + port + "/book/view/4", Book.class);
        assertEquals(bookRepository.findById(4L).orElseThrow().getIsbn(), book.getIsbn());
        assertEquals(bookRepository.findById(4L).orElseThrow().getName(), book.getName());
    }

    @Test
    void testCrearLibro() throws Exception {
        Book bookProof = new Book("1234", "TestBook");
        Book book = rest.postForObject("http://localhost:" + port + "/book/create-book", bookProof, Book.class);
        assertEquals(bookProof.getIsbn(), book.getIsbn());
        assertEquals(bookProof.getName(), book.getName());
    }

    @Test
    void testEditarLibro() throws Exception {
        Book book = bookRepository.findById(4L).orElseThrow();
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/edit-book", HttpMethod.PUT, null, Void.class, book);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testBorrarLibro() throws Exception {
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete/4", HttpMethod.DELETE, null, Void.class, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCrearAutor() throws Exception {
        Author authorProof = new Author("Benito Ocasio");
        Author author = rest.postForObject("http://localhost:" + port + "/book/create-author", authorProof, Author.class);
        assertEquals(authorProof.getName(), author.getName());
    }

    @Test
    void testLinkAuthorBook() throws Exception {
        Author author = authorRepository.findById(2L).orElseThrow();
        Book book = bookRepository.findById(4L).orElseThrow();
        DTOLink dtoLink = new DTOLink(author.getId(), book.getId());
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/link", HttpMethod.PUT, null, Void.class, dtoLink);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUnlinkAuthorBook() throws Exception {
        Author author = authorRepository.findById(1L).orElseThrow();
        Book book = bookRepository.findById(4L).orElseThrow();
        DTOLink dtoLink = new DTOLink(author.getId(), book.getId());
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/unlink", HttpMethod.PUT, null, Void.class, dtoLink);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

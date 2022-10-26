package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.example.dto.DTOLink;
import com.example.dto.DTOLoan;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Loan;
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

    LocalDateTime ldt = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        Loan loan1 = new Loan(formatter.format(ldt), "6/12/2022", realBook1, author3);
        Loan loan2 = new Loan(formatter.format(ldt), "16/12/2022", realBook2, author1);
        Loan loan3 = new Loan(formatter.format(ldt), "14/12/2022", realBook3, author2);
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
        loanRepository.save(loan1);
        loanRepository.save(loan2);
        loanRepository.save(loan3);
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
        HttpEntity<Book> httpEntity = new HttpEntity<Book>(book);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/edit-book", HttpMethod.PUT, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void testBorrarLibro() throws Exception {
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete/4", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCrearAutor() throws Exception {
        Author authorProof = new Author("Benito Ocasio");
        Author author = rest.postForObject("http://localhost:" + port + "/book/create-author", authorProof, Author.class);
        assertEquals(authorProof.getName(), author.getName());
    }

    @Test
    void testEditarAutor    () throws Exception {
        Author author = authorRepository.findById(1L).orElseThrow();
        HttpEntity<Author> httpEntity = new HttpEntity<Author>(author);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/edit-author", HttpMethod.PUT, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBorrarAutor() throws Exception {
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete-author/1", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLinkAuthorBook() throws Exception {
        Author author = authorRepository.findById(2L).orElseThrow();
        Book book = bookRepository.findById(4L).orElseThrow();
        DTOLink dtoLink = new DTOLink(author.getId(), book.getId());
        HttpEntity<DTOLink> httpEntity = new HttpEntity<DTOLink>(dtoLink);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/link", HttpMethod.PUT, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUnlinkAuthorBook() throws Exception {
        Author author = authorRepository.findById(1L).orElseThrow();
        Book book = bookRepository.findById(4L).orElseThrow();
        DTOLink dtoLink = new DTOLink(author.getId(), book.getId());
        HttpEntity<DTOLink> httpEntity = new HttpEntity<DTOLink>(dtoLink);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/unlink", HttpMethod.PUT, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBuscarPrestamo() throws Exception {
        Loan loanProof = loanRepository.findById(10L).orElseThrow();
        Loan loan = rest.getForObject("http://localhost:" + port + "/book/view-loan/10", Loan.class);
        assertEquals(loanProof.getInitDate(), loan.getInitDate());
        assertEquals(loanProof.getFinalDate(), loan.getFinalDate());
    }

    @Test
    void testCrearPrestamo() throws Exception {
        RealBook realBook = realBookRepository.findById(5L).orElseThrow();
        Author author = authorRepository.findById(1L).orElseThrow();
        Loan loanProof = new Loan(formatter.format(ldt), "5/12/2022", realBook, author);
        DTOLoan dtoLoan = new DTOLoan(realBook.getId(), author.getId(), loanProof.getFinalDate());
        Loan loan = rest.postForObject("http://localhost:" + port + "/book/loan", dtoLoan, Loan.class);
        assertEquals(loanProof.getInitDate(), loan.getInitDate());
        assertEquals(loanProof.getFinalDate(), loan.getFinalDate());
    }

    @Test
    void testBorrarPrestamo() throws Exception {
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete-loan/10", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testEditarPrestamo() throws Exception {
        Loan loan = loanRepository.findById(11L).orElseThrow();
        HttpEntity<Loan> httpEntity = new HttpEntity<Loan>(loan);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/edit-loan", HttpMethod.PATCH, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testBuscarCopia() throws Exception {
        RealBook realBookProof = realBookRepository.findById(5L).orElseThrow();
        RealBook realBook = rest.getForObject("http://localhost:" + port + "/book/view-copy/5", RealBook.class);
        assertEquals(realBookProof.getStatus(), realBook.getStatus());
        assertEquals(realBookProof.getTimestamp(), realBook.getTimestamp());
    }

    @Test
    void testCrearCopia() throws Exception {
        RealBook realBookProof = new RealBook("MALO", "1/1/2022");
        RealBook realBook = rest.postForObject("http://localhost:" + port + "/book/create-copy", realBookProof, RealBook.class);
        assertEquals(realBookProof.getStatus(), realBook.getStatus());
        assertEquals(realBookProof.getTimestamp(), realBook.getTimestamp());
    }

    @Test
    void testBorrarCopia() throws Exception {
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete-copy/5", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

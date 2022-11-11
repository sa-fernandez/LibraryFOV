package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.example.dto.DTOCopy;
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
        Book book1 = new Book("8273", "Guía práctica para Spring", "Ficción", "Spring", "Pabli");
        Book book2 = new Book("3421", "Guía práctica para Angular", "Terror", "Angular", "Pipe");
        Book book3 = new Book("9790", "Guía práctica para Testing", "Romance", "Test", "Asderina");
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
        realBook1.setBook(book1);
        realBook2.setBook(book2);
        realBook3.setBook(book3);
        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        realBookRepository.save(realBook1);
        realBookRepository.save(realBook2);
        realBookRepository.save(realBook3);
        loanRepository.save(loan1);
        loanRepository.save(loan2);
        loanRepository.save(loan3);
    }

    @Test
    void testListarLibros() throws Exception {
        List<Book> booksProof = bookRepository.findAll();
        ResponseEntity<List<Book>> response = rest.exchange("/book/list",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Book>>() {
            });
        List<Book> books = response.getBody();
        assertNotNull(books);
        assertEquals(booksProof.size(), books.size());
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
    void testListarAutores() throws Exception {
        List<Author> authorsProof = authorRepository.findAll();
        ResponseEntity<List<Author>> response = rest.exchange("/book/list-authors",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Author>>() {
            });
        List<Author> authors = response.getBody();
        assertNotNull(authors);
        assertEquals(authorsProof.size(), authors.size());
    }

    @Test
    void testBuscarAutor() throws Exception {
        Author author = rest.getForObject("http://localhost:" + port + "/book/view-author/Migue", Author.class);
        assertEquals(authorRepository.findByName("Migue").getId(), author.getId());
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
    void testListarPrestamos() throws Exception {
        List<Loan> loansProof = loanRepository.findAll();
        ResponseEntity<List<Loan>> response = rest.exchange("/book/list-loans",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Loan>>() {
            });
        List<Loan> loans = response.getBody();
        assertNotNull(loans);
        assertEquals(loansProof.size(), loans.size());
    }

    @Test
    void testBuscarPrestamo() throws Exception {
        Loan loanProof = loanRepository.findById(12L).orElseThrow();
        Loan loan = rest.getForObject("http://localhost:" + port + "/book/view-loan/12", Loan.class);
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
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete-loan/12", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testEditarPrestamo() throws Exception {
        Loan loan = loanRepository.findById(12L).orElseThrow();
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
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/delete-copy/8", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testLinkCopyBook() throws Exception {
        RealBook realBook = realBookRepository.findById(8L).orElseThrow();
        Book book = bookRepository.findById(4L).orElseThrow();
        DTOCopy dtoCopy = new DTOCopy(book.getId(), realBook.getId());
        HttpEntity<DTOCopy> httpEntity = new HttpEntity<DTOCopy>(dtoCopy);
        ResponseEntity<Void> response = rest.exchange("http://localhost:" + port + "/book/link-copy", HttpMethod.PUT, httpEntity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testListarCopiasNoPrestadas() throws Exception {
        List<RealBook> copiesProof = realBookRepository.findAllNotBorrowed(4L);
        ResponseEntity<List<RealBook>> response = rest.exchange("/book/not-borrowed/4",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RealBook>>() {
            });
        List<RealBook> copies = response.getBody();
        assertNotNull(copies);
        assertEquals(copiesProof.size(), copies.size());
    }

    @Test
    void testListarPrestamosPersona() throws Exception {
        List<Loan> loansProof = loanRepository.findAllBorrowedPerson(1L);
        ResponseEntity<List<Loan>> response = rest.exchange("/book/list-person-loans/1",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Loan>>() {
            });
        List<Loan> loans = response.getBody();
        assertNotNull(loans);
        assertEquals(loansProof.size(), loans.size());
    }

    @Test
    void testBuscarCopiaPrestamo() throws Exception {
        Loan loan = loanRepository.findById(12L).orElseThrow();
        RealBook realBook = rest.getForObject("http://localhost:" + port + "/book/view-realbook/12", RealBook.class);
        assertEquals(loan.getBook().getStatus(), realBook.getStatus());
        assertEquals(loan.getBook().getTimestamp(), realBook.getTimestamp());
    }

}

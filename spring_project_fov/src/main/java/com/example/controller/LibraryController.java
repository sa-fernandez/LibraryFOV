package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController()
@RequestMapping("/book")
public class LibraryController {
    
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    RealBookRepository realBookRepository;

    Logger log = LoggerFactory.getLogger(getClass());

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list")
    public Iterable<Book> listarLibros(){
        return bookRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/view/{id}")
    public Book buscarLibro(@PathVariable("id") Long id){
        return bookRepository.findById(id).orElseThrow();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/authors/{id}")
    public Iterable<Author> buscarAutores(@PathVariable("id") Long id){
        Book book = this.buscarLibro(id);
        return book.getAuthors();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete/{id}")
    public void borrarLibro(@PathVariable("id") Long id){
        bookRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/create-book")
    public Book crearLibro(@RequestBody Book book){
        return bookRepository.save(book);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/create-author")
    public Author crearAutor(@RequestBody Author author){
        return authorRepository.save(author);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/link")
    public void linkAuthorBook(@RequestBody DTOLink dtoLink){
        Author author = authorRepository.findById(dtoLink.getIdAuthor()).orElseThrow();
        Book book = bookRepository.findById(dtoLink.getIdBook()).orElseThrow();
        book.getAuthors().add(author);
        authorRepository.save(author);
        bookRepository.save(book);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list-authors")
    public Iterable<Author> listarAutores(){
        return authorRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/edit-book")
    public void editarLibro(@RequestBody Book book){
        bookRepository.updateBook(book.getIsbn(), book.getName(), book.getId());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/edit-author")
    public void editarAutor(@RequestBody Author author){
        authorRepository.updateAuthor(author.getName(), author.getId());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete-author/{id}")
    public void borrarAutor(@PathVariable("id") Long id){
        authorRepository.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/unlink")
    public void unlinkAuthorBook(@RequestBody DTOLink dtoLink){
        Author author = authorRepository.findById(dtoLink.getIdAuthor()).orElseThrow();
        Book book = bookRepository.findById(dtoLink.getIdBook()).orElseThrow();
        book.getAuthors().remove(author);
        bookRepository.save(book);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/loan")
    public Loan crearPrestamo(@RequestBody DTOLoan dtoLoan){
        RealBook copy = realBookRepository.findById(dtoLoan.getIdCopy()).orElseThrow();
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Author author = authorRepository.findById(dtoLoan.getIdPerson()).orElseThrow();
        return loanRepository.save(new Loan(formatter.format(ldt), dtoLoan.getFinalDate(), copy, author));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list-copies/{id}")
    public Iterable<RealBook> listarCopias(@PathVariable("id") Long id){
        Book book = bookRepository.findById(id).orElseThrow();
        return book.getCopies();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/view-copy/{id}")
    public RealBook buscarCopia(@PathVariable("id") Long id){
        return realBookRepository.findById(id).orElseThrow();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/book-copy/{id}")
    public Book buscarLibroCopia(@PathVariable("id") Long id){
        RealBook realBook = realBookRepository.findById(id).orElseThrow();
        return realBook.getBook();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list-loans")
    public Iterable<Loan> listarPrestamos(){
        return loanRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/not-borrowed/{id}")
    public Iterable<RealBook> listarCopiasNoPrestadas(@PathVariable("id") Long id){
        return realBookRepository.findAllNotBorrowed(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/list-person-loans/{id}")
    public Iterable<Loan> listarPrestamosPersona(@PathVariable("id") Long id){
        return loanRepository.findAllBorrowedPerson(id);
    }
    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/view-realbook/{id}")
    public RealBook buscarLibroReal(@PathVariable("id") Long id){
        Loan loan = loanRepository.findById(id).orElseThrow();
        return loan.getBook();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/view-loan/{id}")
    public Loan buscarPrestamo(@PathVariable("id") Long id){
        return loanRepository.findById(id).orElseThrow();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete-loan/{id}")
    public void borrarPrestamo(@PathVariable("id") Long id){
        loanRepository.deleteLoan(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PatchMapping("/edit-loan")
    public void aplazarFecha(@RequestBody Loan loan){
        loanRepository.updateLoan(loan.getFinalDate(), loan.getId());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/create-copy")
    public RealBook crearCopia(@RequestBody RealBook realBook){
        return realBookRepository.save(realBook);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/link-copy")
    public void linkCopyBook(@RequestBody DTOCopy dtoCopy){
        RealBook realBook = realBookRepository.findById(dtoCopy.getIdCopy()).orElseThrow();
        Book book = bookRepository.findById(dtoCopy.getIdBook()).orElseThrow();
        book.getCopies().add(realBook);
        realBook.setBook(book);
        bookRepository.save(book);
        realBookRepository.save(realBook);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/delete-copy/{id}")
    public void borrarCopia(@PathVariable("id") Long id){
        realBookRepository.deleteById(id);
    }

}

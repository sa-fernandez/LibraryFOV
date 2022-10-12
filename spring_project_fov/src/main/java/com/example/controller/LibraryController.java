package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.DTOLink;
import com.example.model.Author;
import com.example.model.Book;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;

@RestController()
@RequestMapping("/book")
public class LibraryController {
    
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

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
        Book book = bookRepository.findById(id).orElseThrow();
        if(book != null){
            bookRepository.delete(book);
        }
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
        Author author = authorRepository.findById(id).orElseThrow();
        if(author != null){
            authorRepository.delete(author);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/unlink")
    public void unlinkAuthorBook(@RequestBody DTOLink dtoLink){
        Author author = authorRepository.findById(dtoLink.getIdAuthor()).orElseThrow();
        Book book = bookRepository.findById(dtoLink.getIdBook()).orElseThrow();
        book.getAuthors().remove(author);
        bookRepository.save(book);
    }

    /*@GetMapping("/list")
    public String listBooks(Model model){
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "book-list";
    }

    @GetMapping("/list/{id}")
    public String buscarLibro(Model model, @PathVariable("id") Long id){
        return findBook(model, id);
    }

    

    @GetMapping("/delete/{id}")
    public String deleteBook(Model model, @PathVariable("id") Long id){
        Book book = bookRepository.findById(id).orElseThrow();
        if(book != null){
            bookRepository.delete(book);
            Iterable<Book> books = bookRepository.findAll();
            model.addAttribute("books", books);
            return "book-list";
        }else{
            return "";
        }
    }

    @GetMapping("/edit/{id}")
    public String editBook(Model model, @PathVariable("id") Long id){
        Book book = bookRepository.findById(id).orElseThrow();
        if(book != null){
            model.addAttribute("book", book);
            return "book-edit";
        }else{
            return "";
        }
    }

    @GetMapping("/create")
    public String createBook(Model model){
        model.addAttribute("book", new Book());
        return "book-create";
    }

    @PostMapping("")
    public String save(@ModelAttribute Book book, Model model) {
        bookRepository.save(book);
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "redirect:/book/list";
    }*/

}

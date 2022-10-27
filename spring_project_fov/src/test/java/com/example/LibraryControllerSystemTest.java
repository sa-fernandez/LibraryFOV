package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.example.model.Author;
import com.example.model.Book;
import com.example.model.RealBook;
import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import com.example.repository.LoanRepository;
import com.example.repository.RealBookRepository;

@ActiveProfiles("systemtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LibraryControllerSystemTest {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    RealBookRepository realBookRepository;

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

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-extensions"); // disabling extensions
        // options.setExperimentalOption("useAutomationExtension", false);
        // options.addArguments("start-maximized"); // open Browser in maximized mode
        // options.addArguments("disable-infobars"); // disabling infobars
        // options.addArguments("--disable-dev-shm-usage"); // overcome limited resource
        // problems
        options.merge(DesiredCapabilities.chrome());
        this.driver = new ChromeDriver(options);

        this.wait = new WebDriverWait(driver, 5);

        this.baseUrl = "http://localhost:4200";
    }

    @AfterEach
    void end() {
        driver.quit();
    }

    @Test
    void listarLibros() {
        driver.get(baseUrl + "/book/list");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 3));
        List<WebElement> libros = driver.findElementsByClassName("book-list");
        assertEquals("Guía práctica para Spring\nISBN: 8273", libros.get(0).getText());
        assertEquals("Guía práctica para Angular\nISBN: 3421", libros.get(1).getText());
        assertEquals("Guía práctica para Testing\nISBN: 9790", libros.get(2).getText());
    }

    @Test
    void compararListas() {
        driver.get(baseUrl + "/book/list");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 3));
        List<Book> librosEsperados = bookRepository.findAll();
        List<WebElement> libros = driver.findElementsByClassName("book-list");
        assertEquals(librosEsperados.size(), libros.size());
        for (int i = 0; i < librosEsperados.size(); i++) {
            assertEquals(librosEsperados.get(i).getName() + "\nISBN: " + librosEsperados.get(i).getIsbn(), libros.get(i).getText());
        }
    }
    
}

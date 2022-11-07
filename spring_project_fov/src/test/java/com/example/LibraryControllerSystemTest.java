package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
        // Loan loan1 = new Loan(formatter.format(ldt), "6/12/2022", realBook1, author3);
        // Loan loan2 = new Loan(formatter.format(ldt), "16/12/2022", realBook2, author1);
        // Loan loan3 = new Loan(formatter.format(ldt), "14/12/2022", realBook3, author2);
        book1.getAuthors().add(author1);
        book2.getAuthors().add(author2);
        book3.getAuthors().add(author3);
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
        // loanRepository.save(loan1);
        // loanRepository.save(loan2);
        // loanRepository.save(loan3);
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
        // options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-extensions"); // disabling extensions
        // options.setExperimentalOption("useAutomationExtension", false);
        // options.addArguments("start-maximized"); // open Browser in maximized mode
        // options.addArguments("disable-infobars"); // disabling infobars
        // options.addArguments("--disable-dev-shm-usage"); // overcome limited resource
        // problems
        options.merge(DesiredCapabilities.chrome());
        this.driver = new ChromeDriver(options);

        this.wait = new WebDriverWait(driver, 10);

        this.baseUrl = "http://localhost:4200";
    }

    @AfterEach
    void end() {
        driver.quit();
    }

    String formatBook(Book book){
        return book.getName() + "\nISBN: " + book.getIsbn();
    }

    @Test
    void listarLibros() {
        driver.get(baseUrl + "/book/list");
        List<WebElement> libros = wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 3));
        assertEquals("Guía práctica para Spring\nISBN: 8273", libros.get(0).getText());
        assertEquals("Guía práctica para Angular\nISBN: 3421", libros.get(1).getText());
        assertEquals("Guía práctica para Testing\nISBN: 9790", libros.get(2).getText());
    }

    @Test
    void compararListas() {
        driver.get(baseUrl + "/book/list");
        List<WebElement> libros = wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 3));
        List<Book> librosEsperados = bookRepository.findAll();
        assertEquals(librosEsperados.size(), libros.size());
        for (int i = 0; i < librosEsperados.size(); i++) {
            assertEquals(formatBook(librosEsperados.get(i)), libros.get(i).getText());
        }
    }

    @Test
    void visualizarLibro(){
        driver.get(baseUrl + "/book/view/4");
        Book book = bookRepository.findById(4L).orElseThrow();
        WebElement name = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement isbn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isbn")));
        try{
            wait.until(ExpectedConditions.textToBePresentInElement(name, book.getName()));
            wait.until(ExpectedConditions.textToBePresentInElement(isbn, book.getIsbn()));
        } catch (TimeoutException e){
            fail("Error", e);
        }
    }

    @Test
    void crearLibro(){
        driver.get(baseUrl + "/book/create");
        WebElement inputBookName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement inputBookIsbn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isbn")));
        WebElement button = driver.findElementById("button-create-book");
        Book book = new Book("4895", "Guia practica para testing con Selenium");
        inputBookName.sendKeys(book.getName());
        inputBookIsbn.sendKeys(book.getIsbn());
        button.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 4));
        List<WebElement> libros = driver.findElementsByClassName("book-list");
        assertEquals(formatBook(book), libros.get(libros.size() - 1).getText());
    }

    @Test
    void editarLibro(){
        driver.get(baseUrl + "/book/view/4");
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-edit-book")));
        button.click();
        String newName = "Guia práctica para implementar pruebas de sistema";
        String newIsbn = "9985";
        WebElement inputName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-name")));
        inputName.sendKeys(newName);
        WebElement inputIsbn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-isbn")));
        inputIsbn.sendKeys(newIsbn);
        WebElement buttonUpdate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-modificar")));
        buttonUpdate.click();
        WebElement name = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement isbn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("isbn")));
        try{
            wait.until(ExpectedConditions.textToBePresentInElement(name, newName));
            wait.until(ExpectedConditions.textToBePresentInElement(isbn, newIsbn));
        } catch (TimeoutException e){
            fail("Error", e);
        }
    }

    @Test
    void borrarLibro(){
        driver.get(baseUrl + "/book/view/4");
        Book book = bookRepository.findById(4L).orElseThrow();
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-delete-book")));
        button.click();
        try{
            List<WebElement> libros = wait.until(ExpectedConditions.numberOfElementsToBe(By.className("book-list"), 2));
            for (WebElement libro : libros){
                assertNotEquals(libro.getText(), formatBook(book));
            }
        } catch (TimeoutException e){
            fail("Error", e);
        }
    }

    @Test
    void crearCopia(){
        driver.get(baseUrl + "/book/view/4");
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-create-copy")));
        button.click();
        Select selectStatus = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-status"))));
        selectStatus.selectByIndex(0);
        WebElement inputDate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-date")));
        inputDate.sendKeys("14/12/2022");
        WebElement buttonCreate = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-copy-create")));
        buttonCreate.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("copy-list"), 2));
    }

    @Test
    void borrarCopia(){
        driver.get(baseUrl + "/book/view/4");
        List<WebElement> copias = wait.until(ExpectedConditions.numberOfElementsToBe(By.className("copy-list"), 2));
        WebElement copy = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[contains(@class, 'copy-list')]//button)[1]")));
    }

    @Test
    void buscarPrestamo(){
        driver.get(baseUrl + "/book/return-book");
        Select selectPerson = new Select(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("authors-names"))));
        selectPerson.selectByIndex(0);
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("button-search-loan")));
        button.click();
    }
    
}

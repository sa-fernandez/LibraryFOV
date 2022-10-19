package com.example.model;

import java.util.ArrayList;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;
import com.example.repository.RealBookRepository;

@Component
@Profile({"default"})
public class Database implements CommandLineRunner {

    private static final int NUM_BOOKS = 1000;
    private static final int NUM_AUTHORS = 5;
    private static final int NUM_REAL = 15;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    RealBookRepository realBookRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String[] status = {"BUENO", "REGULAR", "MALO"};

        Random random = new Random(1234);

        RandomStringGenerator randomGen = new RandomStringGenerator.Builder().withinRange('a', 'z')
                .usingRandom(random::nextInt).build();

        for (int i = 0; i < NUM_BOOKS; i++) {
            String name = randomGen.generate(5, 10);
            int isbn = random.nextInt(9999);
            int cantAuthors = random.nextInt(NUM_AUTHORS - 1) + 1;
            ArrayList<Author> authors = new ArrayList<>();
            for (int j = 0; j < cantAuthors; j++) {
                Author author = new Author(randomGen.generate(5, 10));
                authorRepository.save(author);
                authors.add(author);
            }
            Book book = new Book(String.valueOf(isbn), name, authors);
            int cantCopies = random.nextInt(NUM_REAL - 1) + 1;
            ArrayList<RealBook> copies = new ArrayList<>();
            for (int k = 0; k < cantCopies; k++) {
                RealBook copy = new RealBook(status[random.nextInt(3)], randomGen.generate(5), book);
                realBookRepository.save(copy);
                copies.add(copy);
            }
            book.setCopies(copies);
            bookRepository.save(book);
        }

    }

}

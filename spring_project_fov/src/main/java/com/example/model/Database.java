package com.example.model;

import java.util.ArrayList;
import java.util.Random;

import javax.transaction.Transactional;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.repository.AuthorRepository;
import com.example.repository.BookRepository;

@Component
public class Database implements CommandLineRunner {

    private static final int NUM_BOOKS = 1000;
    private static final int NUM_AUTHORS = 5;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
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
            bookRepository.save(new Book(String.valueOf(isbn), name, authors));
        }

    }

}

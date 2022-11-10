package com.example.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String isbn;
    private String name;
    private String category;
    private String description;
    private String editorial;

    @ManyToMany
    @JsonIgnore
    private List<Author> authors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RealBook> copies = new ArrayList<>();

    public Book(){

    }

    public Book(String isbn, String name){
        this.isbn = isbn;
        this.name = name;
    }

    public Book(String isbn, String name, String category, String description, String editorial) {
        this.isbn = isbn;
        this.name = name;
        this.category = category;
        this.description = description;
        this.editorial = editorial;
    }

    public Book(String isbn, String name, ArrayList<Author> authors){
        this.isbn = isbn;
        this.name = name;
        this.authors = authors;
    }

    public Book(String isbn, String name, String category, String description, String editorial, List<Author> authors) {
        this.isbn = isbn;
        this.name = name;
        this.category = category;
        this.description = description;
        this.editorial = editorial;
        this.authors = authors;
    }

    public Book(String isbn, String name, List<Author> authors, List<RealBook> copies) {
        this.isbn = isbn;
        this.name = name;
        this.authors = authors;
        this.copies = copies;
    }

    public Book(String isbn, String name, String category, String description, String editorial, List<Author> authors,
            List<RealBook> copies) {
        this.isbn = isbn;
        this.name = name;
        this.category = category;
        this.description = description;
        this.editorial = editorial;
        this.authors = authors;
        this.copies = copies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<RealBook> getCopies() {
        return copies;
    }

    public void setCopies(List<RealBook> copies) {
        this.copies = copies;
    }
    
}

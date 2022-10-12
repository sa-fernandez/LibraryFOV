package com.example.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String isbn;
    private String name;

    @ManyToMany
    @JsonIgnore
    private List<Author> authors = new ArrayList<>();

    public Book(){

    }

    public Book(String isbn, String name){
        this.isbn = isbn;
        this.name = name;
    }

    public Book(String isbn, String name, ArrayList<Author> authors){
        this.isbn = isbn;
        this.name = name;
        this.authors = authors;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getIsbn(){
        return this.isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Author> getAuthors(){
        return this.authors;
    }

    public void setAuthors(List<Author> authors){
        this.authors = authors;
    }
    
}

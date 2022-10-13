package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Loan {

    @Id
    @GeneratedValue
    private Long id;
    
    private String timestamp;
    
    @OneToOne
    private Book book;

    @OneToOne
    private Author person;

    public Loan(){

    }

    public Loan(String timestamp, Book book, Author person){
        this.timestamp = timestamp;
        this.book = book;
        this.person = person;
    }

    public String getTimestamp(){
        return this.timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public Book getBook(){
        return this.book;
    }

    public void setBook(Book book){
        this.book = book;
    }

    public Author getPerson(){
        return this.person;
    }

    public void setPerson(Author person){
        this.person = person;
    }

}

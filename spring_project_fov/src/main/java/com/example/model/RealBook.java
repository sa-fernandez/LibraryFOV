package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RealBook {

    @Id
    @GeneratedValue
    private Long id;

    private String status;
    private String timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Book book;

    public RealBook(){

    }

    public RealBook(String status, String timestamp){
        this.status = status;
        this.timestamp = timestamp;
    }

    public RealBook(String status, String timestamp, Book book) {
        this.status = status;
        this.timestamp = timestamp;
        this.book = book;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
}

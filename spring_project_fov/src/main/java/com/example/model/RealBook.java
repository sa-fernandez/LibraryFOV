package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RealBook {

    @Id
    @GeneratedValue
    private Long id;

    private String status;
    private String timestamp;

    @ManyToOne
    @JsonIgnore
    private Book book;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private Loan loan;

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

    public RealBook(String status, String timestamp, Book book, Loan loan) {
        this.status = status;
        this.timestamp = timestamp;
        this.book = book;
        this.loan = loan;
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
    
}

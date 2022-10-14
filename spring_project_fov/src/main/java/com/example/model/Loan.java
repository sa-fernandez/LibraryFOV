package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Loan {

    @Id
    @GeneratedValue
    private Long id;
    
    private String initDate;
    private String finalDate;
    
    @OneToOne(cascade = CascadeType.ALL)
    private RealBook book;

    @OneToOne
    private Author person;

    public Loan(){

    }

    public Loan(String initDate, String finalDate, RealBook book, Author person) {
        this.initDate = initDate;
        this.finalDate = finalDate;
        this.book = book;
        this.person = person;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public RealBook getBook() {
        return book;
    }

    public void setBook(RealBook book) {
        this.book = book;
    }

    public Author getPerson() {
        return person;
    }

    public void setPerson(Author person) {
        this.person = person;
    }
    
}

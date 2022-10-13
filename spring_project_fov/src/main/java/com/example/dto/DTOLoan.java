package com.example.dto;

public class DTOLoan {
    
    private Long idBook;
    private String personName;

    public DTOLoan(){

    }

    public DTOLoan(Long idBook, String personName){
        this.idBook = idBook;
        this.personName = personName;
    }

    public Long getIdBook(){
        return this.idBook;
    }

    public void setIdBook(Long idBook){
        this.idBook = idBook;
    }

    public String getPersonName(){
        return this.personName;
    }

    public void setPersonName(String personName){
        this.personName = personName;
    }

}

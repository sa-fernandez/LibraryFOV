package com.example.dto;

public class DTOLink {

    private Long idAuthor;
    private Long idBook;

    public DTOLink(){

    }

    public DTOLink(Long idAuthor, Long idBook){
        this.idAuthor = idAuthor;
        this.idBook = idBook;
    }

    public Long getIdAuthor(){
        return this.idAuthor;
    }

    public void setIdAuthor(Long idAuthor){
        this.idAuthor = idAuthor;
    }

    public Long getIdBook(){
        return this.idBook;
    }

    public void setIdBook(Long idBook){
        this.idBook = idBook;
    }

}

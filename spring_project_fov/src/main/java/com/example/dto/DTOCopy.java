package com.example.dto;

public class DTOCopy {
    
    private Long idBook;
    private Long idCopy;
    
    public DTOCopy() {

    }

    public DTOCopy(Long idBook, Long idCopy) {
        this.idBook = idBook;
        this.idCopy = idCopy;
    }

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public Long getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(Long idCopy) {
        this.idCopy = idCopy;
    }

}

package com.example.dto;

public class DTOLoan {
    
    private Long idCopy;
    private Long idPerson;
    private String finalDate;

    public DTOLoan(){

    }

    public DTOLoan(Long idCopy, Long idPerson, String finalDate) {
        this.idCopy = idCopy;
        this.idPerson = idPerson;
        this.finalDate = finalDate;
    }

    public Long getIdCopy() {
        return idCopy;
    }

    public void setIdCopy(Long idCopy) {
        this.idCopy = idCopy;
    }

    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

}

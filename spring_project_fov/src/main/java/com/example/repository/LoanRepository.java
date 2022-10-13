package com.example.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.model.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {
    
}

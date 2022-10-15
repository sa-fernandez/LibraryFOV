package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.model.Loan;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    @Transactional
    @Query(
        value = "select * from loan where person_id = :id",
        nativeQuery = true
    )
    Iterable<Loan> findAllBorrowedPerson(Long id);

    @Transactional
    @Modifying
    @Query(
        value = "update loan set final_date = :finaldate where id = :id",
        nativeQuery = true
    )
    void updateLoan(@Param("finaldate") String finaldate, @Param("id") Long id);
    
}

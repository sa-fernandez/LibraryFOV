package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.model.RealBook;

public interface RealBookRepository extends CrudRepository<RealBook, Long> {

    @Transactional
    @Query(
        value = "select * from real_book where book_id = :id and id not in ( select book_id from loan )",
        nativeQuery = true
    )
    Iterable<RealBook> findAllNotBorrowed(Long id);
    
}

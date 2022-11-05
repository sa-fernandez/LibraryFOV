package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.RealBook;

public interface RealBookRepository extends JpaRepository<RealBook, Long> {

    @Transactional
    @Query(
        value = "select * from real_book where book_id = :id and id not in ( select book_id from loan )",
        nativeQuery = true
    )
    List<RealBook> findAllNotBorrowed(@Param("id") Long id);
    
}

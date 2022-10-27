package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Modifying
    @Query(
        value = "update book set isbn = :isbn, name = :name where id = :id",
        nativeQuery = true
    )
    void updateBook(@Param("isbn") String isbn, @Param("name") String name, @Param("id") Long id);
    
}

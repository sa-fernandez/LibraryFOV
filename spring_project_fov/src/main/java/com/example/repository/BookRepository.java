package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Query(
        value = "select * from book where id in ( select book_id from real_book where id in ( select book_id from loan where person_id = :id ) )",
        nativeQuery = true
    )
    List<Book> findBooksBorrowed(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(
        value = "update book set isbn = :isbn, name = :name, category = :category, editorial = :editorial, description = :description where id = :id",
        nativeQuery = true
    )
    void updateBook(@Param("isbn") String isbn, @Param("name") String name, @Param("id") Long id, @Param("category") String category, @Param("editorial") String editorial, @Param("description") String description);
    
}

package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Transactional
    @Modifying
    @Query(
        value = "update author set name = :name where id = :id",
        nativeQuery = true
    )
    void updateAuthor(@Param("name") String name, @Param("id") Long id);
    
}

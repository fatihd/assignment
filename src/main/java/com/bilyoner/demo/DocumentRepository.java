package com.bilyoner.demo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {

    Optional<Document> findOneByNumber(int number);

    Document findFirstByOrderByNumberAsc();

    Document findFirstByOrderByNumberDesc();
}
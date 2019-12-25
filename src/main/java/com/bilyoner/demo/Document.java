package com.bilyoner.demo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@org.springframework.data.mongodb.core.mapping.Document
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {

    String id;

    @Indexed(unique = true)
    int number;

    LocalDateTime insertedAt;
}
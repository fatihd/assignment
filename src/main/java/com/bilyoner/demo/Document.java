package com.bilyoner.demo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {

    String id;

    @Indexed(unique = true)
    int number;

    @Field("inserted_at")
    String insertedAt;
}
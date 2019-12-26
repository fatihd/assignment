package com.bilyoner.demo;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentResourceIT {
    @Autowired
    MongoTemplate mongoTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void clearDataBase() {
    }

    @Test
    public void duplicateDocument() throws Exception {
        var response = createDocumentWithNumber(25);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var response2 = createDocumentWithNumber(25);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void minAndMax() throws Exception {
        thereIsNoMinimumDocument();
        thereIsNoMaximumDocument();

        createDocumentWithNumber(25);
        minimumDocumentNumberIs(25);
        maximumDocumentNumberIs(25);

        createDocumentWithNumber(5);
        minimumDocumentNumberIs(5);
        maximumDocumentNumberIs(25);

        createDocumentWithNumber(15);
        minimumDocumentNumberIs(5);
        maximumDocumentNumberIs(25);

        deleteDocumentWithNumber(5);
        minimumDocumentNumberIs(15);
        maximumDocumentNumberIs(25);

        deleteDocumentWithNumber(25);
        minimumDocumentNumberIs(15);
        maximumDocumentNumberIs(15);

        deleteDocumentWithNumber(15);
        thereIsNoMinimumDocument();
        thereIsNoMaximumDocument();
    }

    @Test
    public void order() throws Exception {
        createDocumentWithNumber(25);
        createDocumentWithNumber(5);
        createDocumentWithNumber(15);

        Document[] ascending = getDocumentsAscending();
        numbersOf(ascending).containsExactly(5, 15, 25);

        Document[] descending = getDocumentsDescending();
        numbersOf(descending).containsExactly(25, 15, 5);

        Document[] defaultOrder = getDocumentsDefaultOrder();
        numbersOf(defaultOrder).containsExactly(5, 15, 25);

    }

    private ListAssert<Integer> numbersOf(Document[] ascending) {
        return assertThat(Arrays.stream(ascending).map(Document::getNumber));
    }

    private Document[] getDocumentsDescending() {
        return this.restTemplate.getForObject(getEndPoint() + "?descending=true", Document[].class);
    }

    private Document[] getDocumentsAscending() {
        return this.restTemplate.getForObject(getEndPoint() + "?descending=false", Document[].class);
    }

    private Document[] getDocumentsDefaultOrder() {
        return this.restTemplate.getForObject(getEndPoint(), Document[].class);
    }

    private void deleteDocumentWithNumber(int documentNumber) {
        delete("/{number}", documentNumber);
    }

    private ResponseEntity<Document> createDocumentWithNumber(int documentNumber) {
        return post("", documentWithNumber(documentNumber));
    }

    private void thereIsNoMinimumDocument() {
        assertThat(minimumDocument()).isNull();
    }

    private void thereIsNoMaximumDocument() {
        assertThat(maximumDocument()).isNull();
    }

    private void minimumDocumentNumberIs(int number) {
        Document returnedDocument = minimumDocument();

        assertThat(returnedDocument.getNumber()).isEqualTo(number);
    }

    private void maximumDocumentNumberIs(int number) {
        Document returnedDocument = get("/max");

        assertThat(returnedDocument.getNumber()).isEqualTo(number);
    }

    private Document minimumDocument() {
        return get("/min");
    }

    private Document maximumDocument() {
        return get("/max");
    }

    private Document documentWithNumber(int number) {
        Document document = new Document();
        document.setNumber(number);
        return document;
    }

    private Document get(String url) {
        return this.restTemplate.getForObject(getEndPoint() + url, Document.class);
    }

    private void delete(String url, int number) {
        this.restTemplate.delete(getEndPoint() + url, number);
    }

    private ResponseEntity<Document> post(String url, Document document) {
        return this.restTemplate.postForEntity(getEndPoint() + url, document, Document.class);
    }

    String getEndPoint() {
        return "http://localhost:" + port + "/document";
    }
}
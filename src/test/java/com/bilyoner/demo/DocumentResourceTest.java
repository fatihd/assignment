package com.bilyoner.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DocumentResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void scenario() throws Exception {
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

    private void deleteDocumentWithNumber(int documentNumber) {
        delete("/{number}", documentNumber);
    }

    private void createDocumentWithNumber(int documentNumber) {
        post("", documentWithNumber(documentNumber));
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

    private Document post(String url, Document document) {
        return this.restTemplate.postForObject(getEndPoint() + url, document, Document.class);
    }

    String getEndPoint() {
        return "http://localhost:" + port + "/document";
    }
}
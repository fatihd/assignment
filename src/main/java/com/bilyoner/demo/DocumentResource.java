package com.bilyoner.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentResource {
    DocumentRepository documentRepository;

    @GetMapping
    public Iterable<Document> get() {
        return documentRepository.findAll();
    }

    @GetMapping("/min")
    public Document getMin() {
        return documentRepository.findFirstByOrderByNumberAsc();
    }

    @GetMapping("/max")
    public Document getMax() {
        return documentRepository.findFirstByOrderByNumberDesc();
    }

    @GetMapping("/{number}")
    public Document get(@PathVariable int number) {
        return getDocument(number);
    }

    @PostMapping
    public Document create(@RequestBody Document document) {
        return documentRepository.save(document);
    }

    @PutMapping
    public Document update(@RequestBody Document document) {
        return documentRepository.save(document);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Void> delete(@PathVariable int number) {
        Document document = getDocument(number);

        documentRepository.delete(document);

        return ResponseEntity.noContent().build();
    }

    private Document getDocument(int number) {
        return documentRepository.findOneByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
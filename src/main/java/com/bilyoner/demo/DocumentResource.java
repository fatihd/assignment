package com.bilyoner.demo;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentResource {
    DocumentRepository documentRepository;

    @GetMapping
    public Iterable<Document> get(@RequestParam(required = false) boolean descending) {
        return documentRepository.findAll(getSort(descending));
    }

    private Sort getSort(boolean descending) {
        var byNumber = Sort.by("number");
        return descending ? byNumber.descending() : byNumber;
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
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        document.setInsertedAt(LocalDateTime.now().format(formatter));
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
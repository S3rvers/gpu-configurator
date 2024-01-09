package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.model.ProductDocument;
import com.amalitech.gpuconfigurator.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @CrossOrigin
    @GetMapping("/v1/search/products")
    public ResponseEntity<Iterable<ProductDocument>> findProducts() {
        return ResponseEntity.ok(searchService.findProducts());
    }
}

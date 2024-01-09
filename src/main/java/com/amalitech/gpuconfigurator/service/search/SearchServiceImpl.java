package com.amalitech.gpuconfigurator.service.search;

import com.amalitech.gpuconfigurator.model.ProductDocument;
import com.amalitech.gpuconfigurator.repository.ProductDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final ProductDocumentRepository productDocumentRepository;

    @Override
    public Iterable<ProductDocument> findProducts() {
        return productDocumentRepository.findAll();
    }
}

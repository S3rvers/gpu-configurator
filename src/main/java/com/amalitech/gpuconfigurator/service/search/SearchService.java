package com.amalitech.gpuconfigurator.service.search;

import com.amalitech.gpuconfigurator.model.ProductDocument;

public interface SearchService {
    Iterable<ProductDocument> findProducts();
}

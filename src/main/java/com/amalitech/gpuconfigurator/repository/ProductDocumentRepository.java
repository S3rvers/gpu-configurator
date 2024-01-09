package com.amalitech.gpuconfigurator.repository;

import com.amalitech.gpuconfigurator.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, UUID> {
}

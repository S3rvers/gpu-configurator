package com.amalitech.gpuconfigurator.service;


import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;
    private final UploadImageServiceImpl cloudianryImage;


    public CreateProductResponseDto createProduct(ProductDto request, MultipartFile file) {
        Category category = categoryService.getCategory(request.getCategory());

        var data = this.cloudianryImage.upload(file);

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .imageUrl(Collections.singletonList(data))
                .productId(request.getProductId())
                .build();

        productRepository.save(product);


        return CreateProductResponseDto
                .builder()
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productId(product.getProductId())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .id(product.getId().toString())
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .productName(product.getProductName())
                        .id(product.getId().toString())
                        .productId(product.getProductId())
                        .productDescription(product.getProductDescription())
                        .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                        .imageUrl(product.getImageUrl().toString())
                        .category(product.getCategory())
                        .build()).collect(Collectors.toList());
    }

    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("product not found"));

        return ProductResponse.builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productId(product.getProductId())
                .productDescription(product.getProductDescription())
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .imageUrl(product.getImageUrl().toString())
                .category(product.getCategory())
                .build();

    }


    public List<Product> getAllProduct() {

        var allProducts = productRepository.findAll();

        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        return allProducts;
    }


    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductByProductId(String productId) {
        Optional<Product> product = productRepository.getProductByProductId(productId);

        return product.orElse(new Product());
    }

}

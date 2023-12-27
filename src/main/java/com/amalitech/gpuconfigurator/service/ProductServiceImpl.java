package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.dto.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.ProductDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;

import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public CreateProductResponseDto createProduct(ProductDto request, List<MultipartFile> files) {
        Category category = categoryService.getCategory(request.getCategory());

        List<String> imageUrls = files.stream()
                .map(this.cloudianryImage::upload)
                .collect(Collectors.toList());

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .category(category)
                .imageUrl(imageUrls)
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
        return productRepository.getProductByProductId(productId).orElseThrow(()-> new NotFoundException(productId+ " "+ "Notfound"));
    }

}
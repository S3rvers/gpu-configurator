package com.amalitech.gpuconfigurator.service.product;


import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryServiceImpl;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;
    private final UploadImageServiceImpl cloudinaryImage;


    @Override

    public CreateProductResponseDto createProduct(ProductDto request, List<MultipartFile> files, MultipartFile coverImage) {
        Category category = categoryService.getCategory(request.getCategory());

        List<String> imageUrls = files.stream()
                .map(this.cloudinaryImage::upload)
                .toList();
        String coverImageUrl = cloudinaryImage.uploadCoverImage(coverImage);

        var product = Product
                .builder()
                .productName(request.getProductName())
                .productDescription(request.getProductDescription())
                .productPrice(request.getProductPrice())
                .productBrand(request.getProductBrand())
                .category(category)
                .imageUrl(imageUrls)
                .coverImage(coverImageUrl)
                .inStock(request.getInStock())
                .productId(request.getProductId())
                .build();

        productRepository.save(product);

        return CreateProductResponseDto
                .builder()
                .id(product.getId().toString())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productId(product.getProductId())
                .productBrand(product.getProductBrand())
                .productBrand(product.getProductBrand())
                .productAvailability(product.getProductAvailability())
                .productCategory(category.getCategoryName())
                .coverImage(product.getCoverImage())
                .imageUrl(product.getImageUrl())
                .inStock(product.getInStock())
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .productName(product.getProductName())
                        .id(product.getId().toString())
                        .productId(product.getProductId())
                        .productDescription(product.getProductDescription())
                        .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                        .productBrand(product.getProductBrand())
                        .coverImage(product.getCoverImage())
                        .imageUrl(product.getImageUrl())
                        .inStock(product.getInStock())
                        .build()).toList();
    }

    @Override
    public ProductResponse getProduct(String id) {
        Product product = productRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("product not found"));
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new NotFoundException("category does not exist "));

        return ProductResponse.builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productId(product.getProductId())
                .productDescription(product.getProductDescription())
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .productAvailability(product.getProductAvailability())
                .productBrand(product.getProductBrand())
                .coverImage(product.getCoverImage())
                .inStock(product.getInStock())
                .imageUrl(product.getImageUrl().stream().toList())
                .category(new AttributeResponseDto(category.getCategoryName(), category.getId().toString()))
                .build();
    }


    public Page<ProductResponse> getAllProducts(int page, int size,String sort) {
        if(sort == null) sort = "createdAt";
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<Product> productPage = productRepository.findAll(pageRequest);

        return productPage.map(product -> ProductResponse.builder()
                .productName(product.getProductName())
                .id(product.getId().toString())
                .productId(product.getProductId())
                .productDescription(product.getProductDescription())
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .coverImage(product.getCoverImage())
                .imageUrl(product.getImageUrl())
                .inStock(product.getInStock())
                .build());
    }

    @Override
    public ProductResponse updateProduct(UUID id, ProductUpdateDto updatedProductDto,List<MultipartFile> files, MultipartFile coverImage ) {
        try {
            Product existingProduct = productRepository.getReferenceById(id);

            if (updatedProductDto.getProductName() != null) {
                existingProduct.setProductName(updatedProductDto.getProductName());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductDescription() != null) {
                existingProduct.setProductDescription(updatedProductDto.getProductDescription());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductPrice() != null) {
                existingProduct.setProductPrice(updatedProductDto.getProductPrice());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getProductId() != null) {
                existingProduct.setProductId(updatedProductDto.getProductId());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getInStock() != null) {
                existingProduct.setInStock(updatedProductDto.getInStock());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if(updatedProductDto.getProductBrand() != null){
                existingProduct.setProductBrand(updatedProductDto.getProductBrand());
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (updatedProductDto.getCategory() != null) {
                var newCategory = categoryRepository.findByCategoryName(updatedProductDto.getCategory()).orElseThrow();
                existingProduct.setCategory(newCategory);
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (files != null) {
                List<String> imageUrls = files.stream()
                        .map(this.cloudinaryImage::upload)
                        .toList();
                existingProduct.setImageUrl(imageUrls);
                existingProduct.setUpdatedAt(LocalDateTime.now());
            }
            if (coverImage != null) {
                String coverImageUrl = cloudinaryImage.uploadCoverImage(coverImage);
                existingProduct.setCoverImage(coverImageUrl);
                existingProduct.setUpdatedAt(LocalDateTime.now());

            }

            Product updatedProduct = productRepository.save(existingProduct);
            return mapProductToProductResponse(updatedProduct);
        } catch (NotFoundException e) {
            throw new NotFoundException("No product found");
        }
    }

    private ProductResponse mapProductToProductResponse(Product updatedProduct) {
        return ProductResponse.builder()
                .id(String.valueOf(updatedProduct.getId()))
                .productName(updatedProduct.getProductName())
                .productId(updatedProduct.getProductId())
                .productDescription(updatedProduct.getProductDescription())
                .productPrice(BigDecimal.valueOf(updatedProduct.getProductPrice()))
                .inStock(updatedProduct.getInStock())
                .productAvailability(updatedProduct.getProductAvailability())
                .build();
    }


    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    public List<Product> getNewProducts(){
        LocalDateTime timeRequest = LocalDateTime.now().minusHours(1);
        return productRepository.getBrandNewProducts(timeRequest).orElse(Collections.emptyList());
    }

}

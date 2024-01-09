package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.service.product.FilteringService;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import com.amalitech.gpuconfigurator.util.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UploadImageService cloudinaryImage;
    private final FilteringService filteringService;


    @CrossOrigin
    @PostMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.CREATED)

    public CreateProductResponseDto addProduct(@Valid @ModelAttribute ProductDto request,
                                               @RequestParam("file") List<MultipartFile> files, @RequestParam("coverImage") MultipartFile coverImage) {
        return productService.createProduct(request, files, coverImage);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductId(@PathVariable("productId") String productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<PageResponseDto> getAllProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {

        PageResponseDto productsResponse = new PageResponseDto();

        if (page != null && size != null) {
            Page<ProductResponse> products = productService.getAllProducts(page, size, sort);
            productsResponse.setProducts(products.getContent());
            productsResponse.setTotal(products.getTotalElements());
        } else {
            List<ProductResponse> products = productService.getAllProducts();
            productsResponse.setProducts(products);
            productsResponse.setTotal(products.size());
        }

        return ResponseEntity.ok(productsResponse);
    }


    @CrossOrigin
    @GetMapping("/v1/product")
    public ResponseEntity<PageResponseDto> getAllProductUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String processor
    ) {

        PageResponseDto productsResponse = new PageResponseDto();

        List<ProductResponse> products = new ArrayList<>();

        if (brand != null || price != null || productType != null || processor != null) {
            List<Product> filteredProducts = filteringService.filterProduct(brand, price, productType, processor);
            if (!filteredProducts.isEmpty()) {
                products = new ResponseMapper().getProductResponses(filteredProducts);
                productsResponse.setProducts(products);
                productsResponse.setTotal(products.size());
            } else {
                productsResponse.setProducts(Collections.emptyList());
                productsResponse.setTotal(0);
            }
        } else if (page != null && size != null) {

            Page<ProductResponse> pagedProducts = productService.getAllProducts(page, size, sort);
            products = pagedProducts.getContent();
            productsResponse.setTotal(pagedProducts.getTotalElements());

        } else {
            products = productService.getAllProducts();
            productsResponse.setProducts(products);
            productsResponse.setTotal(products.size());
        }

        productsResponse.setProducts(products);
        return ResponseEntity.ok(productsResponse);
    }


    @CrossOrigin
    @PatchMapping("/v1/admin/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @ModelAttribute ProductUpdateDto updatedProductDto,
            @RequestParam(value = "file", required = false) List<MultipartFile> files,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        ProductResponse updatedProduct = productService.updateProduct(id, updatedProductDto, files, coverImage);
        return ResponseEntity.ok(updatedProduct);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }


}

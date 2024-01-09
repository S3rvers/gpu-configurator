package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.product.*;
import com.amalitech.gpuconfigurator.service.cloudinary.UploadImageService;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final UploadImageService cloudinaryImage;


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
    @GetMapping("/v1/product/{productId}")
    public ResponseEntity<ProductResponse> getProductByProductIdUser(@PathVariable("productId") String productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }


    @CrossOrigin
    @GetMapping("/v1/admin/product")
    public ResponseEntity<PageResponseDto> getAllProducts(
            @RequestParam(required = false) Integer page,
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
    @PatchMapping("/v1/admin/product/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable("id") UUID id,
            @ModelAttribute ProductUpdateDto updatedProductDto,
            @RequestParam(name = "file",required = false) List<MultipartFile> files,
            @RequestParam(name = "coverImage",required = false) MultipartFile coverImage
            ) {

        ProductResponse updatedProduct = productService.updateProduct(id, updatedProductDto,files,coverImage);

        return ResponseEntity.ok(updatedProduct);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
    }


}

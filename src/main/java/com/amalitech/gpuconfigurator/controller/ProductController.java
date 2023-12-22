package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductUpdateDto;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;


    @PostMapping("/v1/admin/product")
    public ResponseEntity<CreateProductResponseDto> addProduct(@Valid @RequestBody ProductDto request){
        CreateProductResponseDto productResponse =  productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/v1/admin/product")
    @ResponseStatus(HttpStatus.OK)
    public List<CreateProductResponseDto> getAllProducts(){
        return productService.getAllProduct();
    }


    @GetMapping("/v1/admin/product/{productId}")
    public ResponseEntity<CreateProductResponseDto> getProductByProductId(@PathVariable("productId") String productId){
        CreateProductResponseDto productResponseDto =  productService.getProductByProductId(productId);
        return ResponseEntity.ok(productResponseDto);
    }

    @PutMapping("/v1/admin/product/{productId}")
    public ResponseEntity<GenericResponse> getProductByProductId(@PathVariable("productId") String productId, @RequestBody ProductUpdateDto product){
        GenericResponse productUpdated =  productService.updateProduct(UUID.fromString(productId), product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productUpdated);
    }


    @DeleteMapping("/v1/admin/product/{id}")
    public void deleteProduct(@PathVariable("id")UUID id) {
        productService.deleteProductById(id);
    }


}

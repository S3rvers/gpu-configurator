package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandNewProductController {

    private final ProductService productService;
    @GetMapping("/v1/new")
    public List<Product> getBrandNewProducts(){
        return productService.getNewProducts();
    }
}

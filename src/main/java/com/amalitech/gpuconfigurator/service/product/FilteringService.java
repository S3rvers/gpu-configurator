package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;

import java.util.List;

public interface FilteringService {
    List<Product> filterProduct(String productCase, String price, String productType, String processor,String categories, String brand);

}

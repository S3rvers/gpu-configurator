package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.CategoryConfig.CategoryConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigOptionsFilteringImpl implements ConfigOptionsFiltering {

    private final ProductRepository productRepository;
    private final CategoryConfigService categoryConfigService;


    public List<UUID> getProductTypes(String productType) {
        List<Product> products = productRepository.findAll();
        List<UUID> productList = new ArrayList<>();

        if (productType != null && !productType.isEmpty()) {
            String[] productTypeList = productType.split(",");
            for (Product product : products) {
                CategoryConfigResponseDto configs = categoryConfigService.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId()));
                for (String type : productTypeList) {
                    if (configs.options().containsKey(type.trim())) {
                        productList.add(product.getId());
                        break;
                    }
                }
            }
        }
        return productList;
    }

}

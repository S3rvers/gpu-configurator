package com.amalitech.gpuconfigurator.service.product;


import com.amalitech.gpuconfigurator.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
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

    public List<UUID> getProcessor(String processor) {
        List<Product> products = productRepository.findAll();
        List<UUID> processorList = new ArrayList<>();

        if (processor != null && !processor.isEmpty()) {
            String[] processorNameList = processor.split(",");
            for (Product product : products) {
                CategoryConfigResponseDto configs = categoryConfigService.getCategoryConfigByCategory(String.valueOf(product.getCategory().getId()));
                for (String name : processorNameList) {
                    if (configs.options().containsKey("Processor") &&
                            configs.options().get("Processor").stream()
                                    .anyMatch(option -> option.name().equalsIgnoreCase(name.trim()))) {
                        processorList.add(product.getId());
                        break;
                    }

                }
            }
        }
        return processorList;
    }


}

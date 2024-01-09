package com.amalitech.gpuconfigurator.service.product;


import java.util.List;
import java.util.UUID;

public interface ConfigOptionsFiltering {
    List<UUID> getProductTypes(String productType);

    List<UUID> getProcessor(String productType);
}

package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;

public interface ConfigurationService {

    ConfigurationResponseDto configuration(String productId, Boolean warranty, String components);

    ConfigurationResponseDto saveConfiguration(String productId, Boolean warranty, String components);

    Configuration getSpecificConfiguration(String configId);

    void deleteSpecificConfiguration(String configId);
}

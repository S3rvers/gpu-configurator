package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;


public interface ConfigurationService {


    ConfigurationResponseDto configuration(String productId, String components, Boolean warranty, Boolean save);
}

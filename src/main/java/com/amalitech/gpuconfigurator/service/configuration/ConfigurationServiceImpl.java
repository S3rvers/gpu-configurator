package com.amalitech.gpuconfigurator.service.configuration;

import com.amalitech.gpuconfigurator.dto.configuration.ConfigurationResponseDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.model.configuration.ConfigOptions;
import com.amalitech.gpuconfigurator.model.configuration.Configuration;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.ConfigurationRepository;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.category.compatible.CompatibleOptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final CompatibleOptionService compatibleOptionService;
    private final ProductRepository productRepository;
    private final CategoryConfigRepository categoryConfigRepository;
    private final ConfigurationRepository configurationRepository;


    @Override
    public ConfigurationResponseDto configuration(String productId, Boolean warranty, Boolean save, String components) {
        Product product = getProductById(productId);
        BigDecimal totalPrice = calculateTotalPrice(product);
        CategoryConfig categoryConfig = getCategoryConfig(product);
        List<CompatibleOption> compatibleOptions = getCompatibleOptions(categoryConfig);
        List<ConfigOptions> configOptions = getConfigOptions(components, compatibleOptions);

        BigDecimal optionalTotal = calculateOptionalTotal(configOptions);
        totalPrice = totalPrice.add(optionalTotal);

        BigDecimal vat = calculateVat(totalPrice);
        BigDecimal totalPriceWithVat = totalPrice.add(vat).setScale(2, RoundingMode.HALF_UP);

        Configuration configuration = createConfiguration(product, configOptions);

        return createResponseDto(configuration, totalPriceWithVat, warranty, vat, optionalTotal, product);
    }

    @Override
    public ConfigurationResponseDto saveConfiguration(String productId, Boolean warranty, Boolean save, String components) {

        Product product = getProductById(productId);
        BigDecimal totalPrice = calculateTotalPrice(product);
        CategoryConfig categoryConfig = getCategoryConfig(product);
        List<CompatibleOption> compatibleOptions = getCompatibleOptions(categoryConfig);
        List<ConfigOptions> configOptions = getConfigOptions(components, compatibleOptions);

        BigDecimal optionalTotal = calculateOptionalTotal(configOptions);
        totalPrice = totalPrice.add(optionalTotal);

        BigDecimal vat = calculateVat(totalPrice);
        BigDecimal totalPriceWithVat = totalPrice.add(vat).setScale(2, RoundingMode.HALF_UP);

        Configuration configuration = createConfiguration(product, configOptions);
        configuration.setTotalPrice(totalPriceWithVat);
        saveConfiguration(configuration);

        return createResponseDto(configuration, totalPriceWithVat, warranty, vat, optionalTotal, product);
    }

    @Override
    public Configuration getSpecificConfiguration(String configId) {
        return configurationRepository.findById(UUID.fromString(configId)).orElseThrow(() -> new NotFoundException("Configuration Not found"));
    }

    @Override
    public void deleteSpecificConfiguration(String configId) {
        configurationRepository.deleteById(UUID.fromString(configId));
    }

    private Product getProductById(String productId) {
        return productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private BigDecimal calculateTotalPrice(Product product) {
        return BigDecimal.valueOf(product.getProductPrice());
    }

    private CategoryConfig getCategoryConfig(Product product) {
        return categoryConfigRepository.findByCategoryId(product.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Configuration does not exist for the specified category"));
    }

    private List<CompatibleOption> getCompatibleOptions(CategoryConfig categoryConfig) {
        return compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());
    }

    private List<ConfigOptions> getConfigOptions(String components, List<CompatibleOption> compatibleOptions) {
        return (components != null && !components.isEmpty()) ?
                getConfigOptionsForComponents(components, compatibleOptions) :
                getConfigOptionsWithoutComponents(compatibleOptions);
    }

    private List<ConfigOptions> getConfigOptionsForComponents(String components, List<CompatibleOption> compatibleOptions) {
        String[] componentIsSizableList = components.split(",");
        return compatibleOptions.stream()
                .filter(option -> Arrays.stream(componentIsSizableList)
                        .map(pair -> pair.split("_")[0])
                        .anyMatch(id -> id.equals(String.valueOf(option.getId()))))
                .map(option -> createConfigOption(option, componentIsSizableList))
                .toList();
    }

    private List<ConfigOptions> getConfigOptionsWithoutComponents(List<CompatibleOption> compatibleOptions) {
        return compatibleOptions.stream()
                .filter(CompatibleOption::getIsIncluded)
                .map(this::createConfigOptionWithoutSize)
                .toList();
    }

    private ConfigOptions createConfigOption(CompatibleOption option, String[] componentIsSizableList) {
        if (Boolean.TRUE.equals(option.getIsMeasured())) {
            String size = Arrays.stream(componentIsSizableList)
                    .filter(pair -> pair.split("_")[0].equals(String.valueOf(option.getId())))
                    .findFirst()
                    .map(pair -> pair.split("_")[1])
                    .orElseGet(() -> String.valueOf(option.getBaseAmount()));

            BigDecimal sizeMultiplier = new BigDecimal(size).divide(BigDecimal.valueOf(option.getBaseAmount())).subtract(new BigDecimal(1));
            BigDecimal calculatedPrice = option.getPrice().multiply(sizeMultiplier).multiply(BigDecimal.valueOf(option.getPriceFactor()));

            return ConfigOptions.builder()
                    .optionId(String.valueOf(option.getId()))
                    .optionName(option.getName())
                    .optionPrice(calculatedPrice)
                    .optionType(option.getType())
                    .baseAmount(BigDecimal.valueOf(option.getBaseAmount()))
                    .isIncluded(option.getIsIncluded())
                    .isMeasured(option.getIsMeasured())
                    .size(size)
                    .build();
        } else {
            return ConfigOptions.builder()
                    .optionId(String.valueOf(option.getId()))
                    .optionName(option.getName())
                    .optionPrice(option.getPrice())
                    .optionType(option.getType())
                    .baseAmount(BigDecimal.valueOf(0))
                    .isIncluded(option.getIsIncluded())
                    .size("1")
                    .isMeasured(option.getIsMeasured())
                    .build();
        }
    }

    private ConfigOptions createConfigOptionWithoutSize(CompatibleOption option) {
        return ConfigOptions.builder()
                .optionId(String.valueOf(option.getId()))
                .optionName(option.getName())
                .optionPrice(option.getPrice().setScale(2, RoundingMode.HALF_UP))
                .optionType(option.getType())
                .baseAmount(BigDecimal.valueOf(0))
                .isIncluded(option.getIsIncluded())
                .size("1")
                .isMeasured(option.getIsMeasured())
                .build();
    }

    private BigDecimal calculateOptionalTotal(List<ConfigOptions> configOptions) {
        return configOptions.stream()
                .map(ConfigOptions::getOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }


    private BigDecimal calculateVat(BigDecimal totalPrice) {
        return BigDecimal.valueOf(25).divide(BigDecimal.valueOf(100)).multiply(totalPrice).setScale(2, RoundingMode.HALF_UP);
    }

    private Configuration createConfiguration(Product product, List<ConfigOptions> configOptions) {
        return Configuration.builder()
                .totalPrice(calculateTotalPrice(product).setScale(2, RoundingMode.HALF_UP))
                .product(product)
                .configuredOptions(configOptions)
                .build();
    }

    private void saveConfiguration(Configuration configuration) {
        configurationRepository.save(configuration);
    }

    private ConfigurationResponseDto createResponseDto(Configuration configuration, BigDecimal totalPriceWithVat,
                                                       Boolean warranty, BigDecimal vat, BigDecimal optionalTotal, Product product) {
        return ConfigurationResponseDto.builder()
                .Id(String.valueOf(configuration.getId()))
                .productName(product.getProductName())
                .productId(String.valueOf(configuration.getProduct().getId()))
                .totalPrice(totalPriceWithVat)
                .warranty(warranty)
                .vat(vat)
                .configuredPrice(optionalTotal)
                .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                .configured(configuration.getConfiguredOptions())
                .build();
    }

}

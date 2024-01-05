package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryConfigResponseDto;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryConfigService {

    private final CategoryConfigRepository categoryConfigRepository;
    private final CategoryRepository categoryRepository;
    private final CompatibleOptionService compatibleOptionService;

    @Transactional
    public GenericResponse createCategoryConfig(UUID id, List<CompatibleOptionDTO> compatibleOptions) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("category not found"));

        CategoryConfig config = CategoryConfig
                .builder()
                .category(category)
                .build();

        CategoryConfig savedCategoryConfig = categoryConfigRepository.save(config);

        List<CompatibleOption> compatibleOptionRequestToResponse = compatibleOptions
                .stream()
                .map(option -> CompatibleOption
                        .builder()
                        .type(option.type())
                        .price(option.price())
                        .name(option.name())
                        .media(option.media())
                        .isCompatible(option.isCompatible())
                        .unit(option.unit())
                        .isIncluded(option.isIncluded())
                        .categoryConfig(savedCategoryConfig)
                        .build()).toList();

        compatibleOptionService.addBulkCompatibleOptions(compatibleOptionRequestToResponse);
        return new GenericResponse(201, "category config created successfully " + config.getId());
    }

    public CategoryConfig getCategoryConfig(String id) {
        return categoryConfigRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("no config found"));
    }

    public CategoryConfigResponseDto getCategoryConfigByCategory(String id) {
        CategoryConfig categoryConfig = categoryConfigRepository.findByCategoryId(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("config does not exist"));
        List<CompatibleOption> compatibleOptions = compatibleOptionService.getByCategoryConfigId(categoryConfig.getId());

        AttributeResponseDto categoryResponse = AttributeResponseDto
                .builder()
                .id(categoryConfig.getCategory().getId().toString())
                .name(categoryConfig.getCategory().getCategoryName())
                .build();

        Map<String, List<CompatibleOptionResponseDto>> compatibleGroupedByType = compatibleOptions.stream()
                .collect(Collectors.groupingBy(CompatibleOption::getType,                           // Group by 'type'
                        Collectors.mapping(option -> CompatibleOptionResponseDto.builder()      // Map each group to a list of CompatibleOptionResponseDto
                                        .id(option.getId().toString())
                                        .type(option.getType())
                                        .price(option.getPrice())
                                        .name(option.getName())
                                        .media(option.getMedia())
                                        .isCompatible(option.getIsCompatible())
                                        .unit(option.getUnit())
                                        .isIncluded(option.getIsIncluded())
                                        .build(),
                                Collectors.toList())));

        return CategoryConfigResponseDto.builder()
                .id(categoryConfig.getId().toString())
                .category(categoryResponse)
                .options(compatibleGroupedByType)
                .build();
    }
}

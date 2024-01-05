package com.amalitech.gpuconfigurator.service.categoryConfig;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionRequest;
import com.amalitech.gpuconfigurator.model.CategoryConfig;
import com.amalitech.gpuconfigurator.model.CompatibleOption;
import com.amalitech.gpuconfigurator.repository.CategoryConfigRepository;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompatibleOptionService {

    private final CompatibleOptionRepository compatibleOptionRepository;
    private final CategoryConfigRepository categoryConfigRepository;

    @Transactional
    public void addBulkCompatibleOptions(List<CompatibleOption> compatibleOptions) {
        compatibleOptionRepository.saveAll(compatibleOptions);
    }

    public CompatibleOptionRequest getCompatibleOption(String id) {
        CompatibleOption compatibleOption = compatibleOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("compatible option does not exist"));

        return CompatibleOptionRequest
                .builder()
                .name(compatibleOption.getName())
                .type(compatibleOption.getType())
                .price(compatibleOption.getPrice())
                .media(compatibleOption.getMedia())
                .unit(compatibleOption.getUnit())
                .isCompatible(compatibleOption.getIsCompatible())
                .categoryConfig(compatibleOption.getCategoryConfig().getId().toString())
                .isIncluded(compatibleOption.getIsIncluded())
                .build();
    }

    public GenericResponse addCompatibleOption(CompatibleOptionRequest option) {
        CategoryConfig categoryConfig = categoryConfigRepository.findById(UUID.fromString(option.categoryConfig())).orElseThrow(() -> new EntityNotFoundException("could not find the category config"));
        CompatibleOption compatibleOption = CompatibleOption.builder().unit(option.unit()).media(option.media()).isCompatible(option.isCompatible()).price(option.price()).name(option.name()).type(option.type()).isIncluded(option.isIncluded()).isCompatible(option.isCompatible()).categoryConfig(categoryConfig).build();
        compatibleOptionRepository.save(compatibleOption);
        return new GenericResponse(201, "compatible option created");
    }

    public GenericResponse updateCompatible(String id, CompatibleOptionRequest compatibleOptionDTO) {
        CompatibleOption compatible = compatibleOptionRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("could not update option"));
        CategoryConfig categoryConfig = categoryConfigRepository.findById(compatible.getCategoryConfig().getId()).orElseThrow(() -> new EntityNotFoundException("could not find the category config"));

        compatible.setName(compatibleOptionDTO.name());
        compatible.setType(compatibleOptionDTO.type());
        compatible.setPrice(compatibleOptionDTO.price());
        compatible.setMedia(compatibleOptionDTO.media());
        compatible.setUnit(compatibleOptionDTO.unit());
        compatible.setCategoryConfig(categoryConfig);
        compatible.setIsCompatible(compatibleOptionDTO.isCompatible());
        compatible.setIsIncluded(compatibleOptionDTO.isIncluded());

        compatibleOptionRepository.save(compatible);

        return new GenericResponse(201, "compatibility is updated " + compatible.getId().toString());
    }

    public GenericResponse deleteAllByCategoryConfigId(String id) {
        compatibleOptionRepository.deleteAllByCategoryConfigId(UUID.fromString(id));
        return new GenericResponse(204, "deleted all category config compatible options of " + id);
    }

    public GenericResponse deleteCompatibleOption(String id) {
        compatibleOptionRepository.deleteById(UUID.fromString(id));
        return new GenericResponse(201, "deleted compatible option");
    }

    public List<CompatibleOption> getByCategoryConfigId(UUID id) {
        return compatibleOptionRepository.getByCategoryConfigId(id);
    }
}

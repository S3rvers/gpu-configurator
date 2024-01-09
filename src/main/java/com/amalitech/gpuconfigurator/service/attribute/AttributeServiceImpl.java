package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeOptionRepository attributeOptionRepository;

    @Override
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAll();

        return attributes.stream()
                .map(this::createAttributeResponseType)
                .collect(Collectors.toList());
    }

    @Override
    public Attribute addAttribute(@NotNull AttributeDto attribute) {
        Attribute newAttribute =  Attribute.builder()
                .attributeName(attribute.attributeName())
                .build();

        return attributeRepository.save(newAttribute);
    }

    @Override
    public AttributeResponse updateAttribute(UUID id, @NotNull AttributeDto attribute) {
        Attribute updateAttribute = attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute not found"));

        updateAttribute.setAttributeName(attribute.attributeName());
        updateAttribute.setUpdatedAt(LocalDateTime.now());

        Attribute savedAttribute = attributeRepository.save(updateAttribute);
        return this.createAttributeResponseType(savedAttribute);
   }

   @Override
   public Attribute getAttributeByName(String name) {
       return attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException("attribute not found"));
   }

    @Override
    public AttributeResponse getAttributeById(UUID id) {
        Attribute attribute = attributeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute not found"));
        return this.createAttributeResponseType(attribute);
    }

    @Override
    public void deleteAttributeByName(String name) {
        Attribute attribute = attributeRepository.findByAttributeName(name).orElseThrow(() -> new EntityNotFoundException("Attribute not found with name: " + name));
        attributeRepository.delete(attribute);
    }

    @Override
    public GenericResponse deleteAttributeById(UUID attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException("Attribute not found with id: " + attributeId));
        attributeRepository.delete(attribute);

        return GenericResponse.builder().status(HttpStatus.ACCEPTED.value()).message("deleted attribute successfully").build();
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id) {
        List<AttributeOption> attr =  attributeOptionRepository.findAllByAttributeId(id).orElseThrow(() -> new EntityNotFoundException("could not find attributes options"));
        return this.streamAttributeOptions(attr);
    }


    @Override
    public List<AttributeOption> getAllAttributeOptionByAttributeName(String name) {
        Attribute attributeByName = this.getAttributeByName(name);
        return attributeOptionRepository.findAllByAttributeId(UUID.fromString(attributeByName.getAttributeName())).orElseThrow(() -> new EntityNotFoundException("could not find attributes options"));
    }

    @Override
    public List<AttributeOptionResponseDto> getAllAttributeOptions() {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAll();
        return this.streamAttributeOptions(attributeOptions);
    }

    @Override
    public AttributeOptionResponseDto getAttributeOptionById(UUID id) {
        AttributeOption attribute = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute with this id does not exist"));
        return this.createAttributeResponseType(attribute);
    }

    @Override
    public GenericResponse deleteAttributeOption(UUID id) {
        attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute with this id does not exist"));
        attributeOptionRepository.deleteById(id);

        return GenericResponse.builder().status(HttpStatus.ACCEPTED.value()).message("deleted attribute option successfully").build();
    }

    @Override
    public AttributeOption getAttributeOptionByName(String name) {
        return attributeOptionRepository.findByOptionName(name).orElseThrow(() -> new EntityNotFoundException("option by this name does not exist"));
    }

    @Override
    public void deleteAttributeOptionByName(String name){ attributeOptionRepository.deleteByOptionName(name); }

    @Override
    public AttributeOptionResponseDto updateAttributeOption(UUID id, @NotNull AttributeOptionDto atrDto) {
        AttributeOption updateAtr = attributeOptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attribute option does not exist"));

        updateAtr.setPriceAdjustment(atrDto.price());
        updateAtr.setOptionName(atrDto.name());
        updateAtr.setUpdatedAt(LocalDateTime.now());

        AttributeOption savedAttribute =  attributeOptionRepository.save(updateAtr);
        return this.createAttributeResponseType(savedAttribute);
    }

    @Override
    public AttributeOptionResponseDto createAttributeOption(UUID attributeId, @NotNull AttributeOptionDto atr) {
        var attr = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException("could not create option for this attribute type"));
        var newAttributeOption = AttributeOption.builder()
                .optionName(atr.name())
                .priceAdjustment(atr.price())
                .attribute(attr)
                .build();

        AttributeOption savedAttribute = attributeOptionRepository.save(newAttributeOption);
        return this.createAttributeResponseType(savedAttribute);
    }



    private List<AttributeOptionResponseDto> streamAttributeOptions(@NotNull List<AttributeOption> instance) {
        return instance
                .stream()
                .map(this::createAttributeResponseType)
                .collect(Collectors.toList());
    }

    private AttributeOptionResponseDto createAttributeResponseType(@NotNull AttributeOption attr) {
        return AttributeOptionResponseDto
                .builder()
                .id(attr.getId().toString())
                .optionName(attr.getOptionName())
                .optionPrice(attr.getPriceAdjustment())
                .attribute(new AttributeResponseDto(attr.getAttribute().getAttributeName(), attr.getAttribute().getId().toString()))
                .build();
    }

    private AttributeResponse createAttributeResponseType(@NotNull Attribute attribute) {
        return AttributeResponse
                .builder()
                .id(attribute.getId())
                .attributeName(attribute.getAttributeName())
                .build();
    }


}

package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.*;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionGetResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionResponseDto;
import com.amalitech.gpuconfigurator.exception.AttributeNameAlreadyExistsException;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeOptionRepository;
import com.amalitech.gpuconfigurator.repository.attribute.AttributeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    public CompatibleOptionGetResponse getAllAttributeOptionsEditable() {
        List<AttributeOption> attributeOptionList = attributeOptionRepository.findAll();

        List<CompatibleOptionResponseDto> compatibleOptionResponseDtoList =  attributeOptionList.stream().map(
                attributeOption -> CompatibleOptionResponseDto.
                        builder()
                        .compatibleOptionId(attributeOption.getId().toString())
                        .name(attributeOption.getOptionName())
                        .type(attributeOption.getAttribute().getAttributeName())
                        .price(attributeOption.getPriceAdjustment())
                        .media(attributeOption.getMedia())
                        .unit(attributeOption.getAttribute().getUnit())
                        .isMeasured(attributeOption.getAttribute().isMeasured())
                        .priceFactor(attributeOption.getPriceFactor())
                        .priceIncrement(null)
                        .baseAmount(attributeOption.getBaseAmount())
                        .maxAmount(attributeOption.getMaxAmount())
                        .isCompatible(true)
                        .isIncluded(false)
                        .attributeOptionId(attributeOption.getId().toString())
                        .attributeId(attributeOption.getAttribute().getId().toString())
                        .size((int) Math.round(attributeOption.getBaseAmount()))
                        .build()
        ).toList();

        return CompatibleOptionGetResponse
                .builder()
                .name("")
                .id(null)
                .config(compatibleOptionResponseDtoList)
                .build();
    }


    @Override
    @Transactional
    public List<AttributeResponse> createAttributeAndAttributeOptions(CreateAttributesRequest createAttributesRequest) throws AttributeNameAlreadyExistsException {
        AttributeDto attributeDto = AttributeDto
                .builder()
                .attributeName(createAttributesRequest.attributeName())
                .description(createAttributesRequest.description())
                .isMeasured(createAttributesRequest.isMeasured())
                .unit(createAttributesRequest.unit())
                .build();

        Attribute createAttribute = this.addAttribute(attributeDto);

        this.createAllAttributeOptions(createAttribute.getId(), createAttributesRequest.variantOptions());
        return this.getAllAttributes();
    }

    @Override
    @Transactional
    public List<AttributeResponse> bulkUpdateAttributeAndAttributeOptions(@NotNull UpdateAttributeDto updateAttributeDto) {

        AttributeDto attributeDto = AttributeDto
                .builder()
                .attributeName(updateAttributeDto.attributeName())
                .description(updateAttributeDto.description())
                .isMeasured(updateAttributeDto.isMeasured())
                .unit(updateAttributeDto.unit())
                .build();

        Attribute attribute = this.updateAttribute(UUID.fromString(updateAttributeDto.id()), attributeDto);
        this.updateAllAttributeOptions(attribute, updateAttributeDto.variantOptions());

        return this.getAllAttributes();
    }

    @Override
    public Attribute addAttribute(@NotNull AttributeDto attribute) throws AttributeNameAlreadyExistsException {
        if(attributeRepository.existsByAttributeName(attribute.attributeName())) throw new AttributeNameAlreadyExistsException("duplicate name already exists");
            Attribute newAttribute = Attribute.builder()
                    .attributeName(attribute.attributeName())
                    .isMeasured(attribute.isMeasured())
                    .description(attribute.description())
                    .unit(attribute.unit())
                    .build();

            return attributeRepository.save(newAttribute);
    }

    @Override
    public Attribute updateAttribute(UUID id, @NotNull AttributeDto attribute) {
        Attribute updateAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        updateAttribute.setAttributeName(attribute.attributeName());
        updateAttribute.setMeasured(attribute.isMeasured());
        updateAttribute.setDescription(attribute.description());
        updateAttribute.setUnit(attribute.unit());
        updateAttribute.setUpdatedAt(LocalDateTime.now());

        return attributeRepository.save(updateAttribute);
   }


    @Override
    public AttributeResponse getAttributeById(UUID id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));

        return this.createAttributeResponseType(attribute);
    }

    @Override
    public GenericResponse deleteAttributeById(UUID attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST +  attributeId));
        attributeRepository.delete(attribute);

        return new GenericResponse(HttpStatus.ACCEPTED.value(),"deleted attribute successfully");
    }

    @Override
    public GenericResponse deleteAttributeOption(UUID attributeId, UUID optionId) {
        var attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        attribute.getAttributeOptions()
                .removeIf(option -> option.getId().equals(optionId));
        attributeRepository.save(attribute);

        return GenericResponse.builder()
                .status(HttpStatus.OK.value())
                .message("deleted attribute option successfully")
                .build();
    }

    @Override
    public void updateAllAttributeOptions(Attribute attribute, List<UpdateAttributeOptionDto> attributeOptionDtos) {
        for(UpdateAttributeOptionDto attributeOption : attributeOptionDtos) {

            AttributeOption updateAttribute = attributeOptionRepository.findById(UUID.fromString(attributeOption.id()))
                    .orElseGet(() -> {
                        AttributeOption newOption = new AttributeOption();
                        return newOption;
                    });

            updateAttribute.setPriceAdjustment(attributeOption.price());
            updateAttribute.setAttribute(attribute);
            updateAttribute.setOptionName(attributeOption.name());
            updateAttribute.setBaseAmount(attributeOption.baseAmount());
            updateAttribute.setMaxAmount(attributeOption.maxAmount());
            updateAttribute.setPriceFactor(attributeOption.priceFactor());
            updateAttribute.setMedia(attributeOption.media());
            updateAttribute.setUpdatedAt(LocalDateTime.now());

            AttributeOption savedAttribute =  attributeOptionRepository.save(updateAttribute);
        }
    }

    @Override
    @Transactional
    public List<AttributeOptionResponseDto> createAllAttributeOptions(UUID attributeId, @NotNull List<CreateAttributeOptionRequest> attributeOptionDtoList) {
        Attribute attribute = attributeRepository.findById(attributeId).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_NOT_EXIST));
        List<AttributeOption> attributeOptionList = attributeOptionDtoList.stream().map(attributes -> AttributeOption.builder()
                .optionName(attributes.name())
                .priceAdjustment(attributes.price())
                .attribute(attribute)
                .media(attributes.media())
                .baseAmount(attributes.baseAmount())
                .maxAmount(attributes.maxAmount())
                .priceFactor(attributes.priceFactor())
                .build()).toList();

        List<AttributeOption> savedAttributeOptions = attributeOptionRepository.saveAll(attributeOptionList);
        return savedAttributeOptions.stream().map(this::createAttributeResponseType).toList();
    }

    @Override
    public GenericResponse deleteBulkAttributes(List<String> selectedAttributes){
        List<UUID> selectedAttributesUUID = selectedAttributes.stream()
                .map(UUID::fromString)
                .toList();

        attributeRepository.deleteAllById(selectedAttributesUUID);
        return new GenericResponse(HttpStatus.ACCEPTED.value(),"deleted bulk attributes successful");
    }

    private List<AttributeOptionResponseDto> streamAttributeOptions(@NotNull List<AttributeOption> instance) {
        return instance
                .stream()
                .map(this::createAttributeResponseType)
                .toList();
    }

    private AttributeOptionResponseDto createAttributeResponseType(@NotNull AttributeOption attributeOption) {
        return AttributeOptionResponseDto
                .builder()
                .id(attributeOption.getId().toString())
                .optionName(attributeOption.getOptionName())
                .optionPrice(attributeOption.getPriceAdjustment())
                .additionalInfo(new AttributeVariantDto(attributeOption.getBaseAmount(), attributeOption.getMaxAmount(), attributeOption.getPriceFactor()))
                .optionMedia(attributeOption.getMedia())
                .attribute(
                        new AttributeResponseDto(
                        attributeOption.getAttribute().getAttributeName(),
                        attributeOption.getAttribute().getId().toString(),
                        attributeOption.getAttribute().isMeasured()))
                .build();
    }

    private AttributeResponse createAttributeResponseType(@NotNull Attribute attribute) {
        List<AttributeOption> attributeOptions = attributeOptionRepository.findAllByAttributeId(attribute.getId()).orElseThrow(() -> new EntityNotFoundException(AttributeConstant.ATTRIBUTE_OPTIONS_NOT_EXIST));
        return AttributeResponse
                .builder()
                .id(attribute.getId())
                .attributeName(attribute.getAttributeName())
                .isMeasured(attribute.isMeasured())
                .description(attribute.getDescription())
                .unit(attribute.getUnit())
                .attributeOptions(this.streamAttributeOptions(attributeOptions))
                .build();
    }
}

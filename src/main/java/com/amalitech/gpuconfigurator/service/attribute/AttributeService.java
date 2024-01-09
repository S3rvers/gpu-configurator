package com.amalitech.gpuconfigurator.service.attribute;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.model.attributes.AttributeOption;

import java.util.List;
import java.util.UUID;

public interface AttributeService {
    List<AttributeResponse> getAllAttributes();

    Attribute addAttribute(AttributeDto attribute);

    AttributeResponse updateAttribute(UUID id, AttributeDto attribute);

    Attribute getAttributeByName(String name);

    AttributeResponse getAttributeById(UUID id);

    void deleteAttributeByName(String name);

    GenericResponse deleteAttributeById(UUID attributeId);

    List<AttributeOptionResponseDto> getAllAttributeOptionByAttributeId(UUID id);

    List<AttributeOption> getAllAttributeOptionByAttributeName(String name);

    List<AttributeOptionResponseDto> getAllAttributeOptions();

    AttributeOptionResponseDto getAttributeOptionById(UUID id);

    GenericResponse deleteAttributeOption(UUID id);

    AttributeOption getAttributeOptionByName(String name);

    void deleteAttributeOptionByName(String name);

    AttributeOptionResponseDto updateAttributeOption(UUID id, AttributeOptionDto atrDto);

    AttributeOptionResponseDto createAttributeOption(UUID attributeId, AttributeOptionDto atr);
}

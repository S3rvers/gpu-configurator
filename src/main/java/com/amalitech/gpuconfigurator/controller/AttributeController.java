package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeOptionResponseDto;
import com.amalitech.gpuconfigurator.dto.attribute.AttributeResponse;
import com.amalitech.gpuconfigurator.model.attributes.Attribute;
import com.amalitech.gpuconfigurator.service.attribute.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping("/v1/admin/attributes")
    public ResponseEntity<Attribute> createAttribute(@Validated  @RequestBody AttributeDto atr) {
        Attribute attribute = attributeService.addAttribute(atr);
        return ResponseEntity.status(HttpStatus.CREATED).body(attribute);
    }

    @GetMapping("/v1/admin/attributes")
    public ResponseEntity<List<AttributeResponse>> getAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<AttributeResponse> getAttribute(@PathVariable String attributeId) {
        AttributeResponse attribute = attributeService.getAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(attribute);
    }

    @PutMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<AttributeResponse> updateAttribute(@PathVariable String attributeId, @RequestBody  AttributeDto atr) {
        AttributeResponse attribute = attributeService.updateAttribute(UUID.fromString(attributeId), atr);
        return ResponseEntity.status(HttpStatus.CREATED).body(attribute);
    }

    @DeleteMapping("/v1/admin/attributes/{attributeId}")
    public ResponseEntity<GenericResponse> deleteAttribute(@PathVariable String attributeId) {
        GenericResponse deleteResponse = attributeService.deleteAttributeById(UUID.fromString(attributeId));
        return ResponseEntity.ok(deleteResponse);
    }

    @PostMapping("/v1/admin/attributes/{attributeId}/options")
    public ResponseEntity<AttributeOptionResponseDto> createAttributeOption(@PathVariable String attributeId, @Validated @RequestBody AttributeOptionDto atrOption) {
        AttributeOptionResponseDto option = attributeService.createAttributeOption(UUID.fromString(attributeId), atrOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(option);
    }

    @GetMapping("/v1/admin/attributes/options")
    public ResponseEntity<List<AttributeOptionResponseDto>> getAllOptions() {
         List<AttributeOptionResponseDto> attributeOptions = attributeService.getAllAttributeOptions();
         return ResponseEntity.ok(attributeOptions);
    }

    @GetMapping("/v1/admin/attributes/{attributeId}/options")
    public ResponseEntity<List<AttributeOptionResponseDto>> getAllAttributeOptions(@PathVariable String attributeId) {
        List<AttributeOptionResponseDto> attributeOptions = attributeService.getAllAttributeOptionByAttributeId(UUID.fromString(attributeId));
        return ResponseEntity.ok(attributeOptions);
    }

    @GetMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<AttributeOptionResponseDto> getOption(@PathVariable String optionId) {
        AttributeOptionResponseDto attributeOption = attributeService.getAttributeOptionById(UUID.fromString(optionId));
        return ResponseEntity.ok(attributeOption);
    }
    @PutMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<AttributeOptionResponseDto> updateOption(@PathVariable String optionId, @RequestBody AttributeOptionDto attr) {
        AttributeOptionResponseDto attributeOption = attributeService.updateAttributeOption(UUID.fromString(optionId), attr);
        return ResponseEntity.ok(attributeOption);
    }

    @DeleteMapping("/v1/admin/attributes/options/{optionId}")
    public ResponseEntity<GenericResponse> deleteOption(@PathVariable String optionId) {
        GenericResponse attributeOption = attributeService.deleteAttributeOption(UUID.fromString(optionId));
        return ResponseEntity.ok(attributeOption);
    }




}

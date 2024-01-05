package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionDTO;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CompatibleOptionRequest;
import com.amalitech.gpuconfigurator.repository.CompatibleOptionRepository;
import com.amalitech.gpuconfigurator.service.categoryConfig.CompatibleOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompatibleOptionsController {

    private final CompatibleOptionService compatibleOptionService;


    @PostMapping("/v1/admin/compatibleOption")
    public ResponseEntity<GenericResponse> createCategoryConfigCompatibleOption(@RequestBody  CompatibleOptionRequest compatibleOption) {
        GenericResponse savedCompatibleOption = compatibleOptionService.addCompatibleOption(compatibleOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompatibleOption);
    }

    @PutMapping("/v1/admin/compatibleOption/{compatibleOptionId}")
    public ResponseEntity<GenericResponse> updateCategoryConfigCompatibleOption(@PathVariable String compatibleOptionId, @RequestBody CompatibleOptionRequest compatibleOption) {
        GenericResponse savedCompatibleOption = compatibleOptionService.updateCompatible(compatibleOptionId, compatibleOption);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompatibleOption);
    }

    @GetMapping("/v1/admin/compatibleOption/{compatibleOptionId}")
    public ResponseEntity<CompatibleOptionRequest> getCategoryConfigCompatibleOption(@PathVariable String compatibleOptionId) {
        CompatibleOptionRequest foundCompatibleOption = compatibleOptionService.getCompatibleOption(compatibleOptionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(foundCompatibleOption);
    }

    @DeleteMapping("/v1/admin/compatibleOption/{compatibleOptionId}")
    public ResponseEntity<GenericResponse> deleteCategoryConfigCompatibleOption(@PathVariable String compatibleOptionId) {
        GenericResponse deletedCompatibleOptionMessage = compatibleOptionService.deleteCompatibleOption(compatibleOptionId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedCompatibleOptionMessage);
    }

    @DeleteMapping("/v1/admin/compatibleOption/config/{categoryConfigId}")
    public ResponseEntity<GenericResponse> deleteAllCategoryConfigCompatibleOption(@PathVariable String categoryConfigId) {
        GenericResponse deletedCompatibleOptionMessage = compatibleOptionService.deleteAllByCategoryConfigId(categoryConfigId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedCompatibleOptionMessage);
    }

}

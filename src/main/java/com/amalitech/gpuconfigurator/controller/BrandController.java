package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @CrossOrigin
    @PostMapping("/v1/admin/brand")
    public ResponseEntity<Brand> createBrand(@Validated  @RequestBody BrandRequest request) {
        Brand brand = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(brand);
    }

    @CrossOrigin
    @GetMapping("/v1/admin/brand")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<BrandDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @CrossOrigin
    @DeleteMapping("/v1/admin/brand/{brandId}")
    public ResponseEntity<GenericResponse> deleteBrand(@PathVariable String brandId) {
        GenericResponse deletedBrand = brandService.deleteBrand(UUID.fromString(brandId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBrand);
    }

    @CrossOrigin
    @PutMapping("/v1/admin/brand/{brandId}")
    public ResponseEntity<GenericResponse> updatedBrand(@PathVariable String brandId, @RequestBody BrandRequest request) {
        GenericResponse updatedBrand = brandService.updatedBrand(UUID.fromString(brandId), request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedBrand);
    }

}

package com.amalitech.gpuconfigurator.service.brand;

import com.amalitech.gpuconfigurator.dto.GenericResponse;
import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    Brand createBrand(BrandRequest request);

    List<BrandDto> getAllBrands();

    GenericResponse deleteBrand(UUID id);

    GenericResponse updatedBrand(UUID id, @NotNull BrandRequest request);
}

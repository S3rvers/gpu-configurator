package com.amalitech.gpuconfigurator.service;


import com.amalitech.gpuconfigurator.dto.brand.BrandDto;
import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.repository.BrandRepository;
import com.amalitech.gpuconfigurator.service.brand.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Test
    public void testCreateBrandService(){
        BrandRequest brand = new BrandRequest("testBrand");
        BrandServiceImpl mockBrandService = Mockito.mock(BrandServiceImpl.class);


        Brand brandClass = Brand
                .builder()
                .name(brand.name())
                .build();

        when(mockBrandService.createBrand(brand)).thenReturn(brandClass);

        Brand result = mockBrandService.createBrand(brand);

        assertNotNull(result);
        verify(mockBrandService, times(1)).createBrand(brand);
    }

    @Test
    public void testListAllBrandService() {
        Brand brand1 = Brand.builder().name("kaycy").id(UUID.randomUUID()).build();
        Brand brand2 = Brand.builder().name("kaycy").id(UUID.randomUUID()).build();
        List<Brand> brands = Arrays.asList(brand1, brand2);

        BrandServiceImpl mockBrandServiceImpl = Mockito.mock(BrandServiceImpl.class);
        when(mockBrandServiceImpl.getAllBrands()).thenReturn(brands.stream().map(brand -> new BrandDto(brand.getName(), brand.getId().toString())).toList());
        List<BrandDto> result = mockBrandServiceImpl.getAllBrands();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.brand.BrandRequest;
import com.amalitech.gpuconfigurator.model.Brand;
import com.amalitech.gpuconfigurator.repository.BrandRepository;
import com.amalitech.gpuconfigurator.service.brand.BrandServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class BrandControllerTest {

    @InjectMocks
    private BrandController brandController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void should_beAble_toAddBrandSuccess() throws Exception {
        BrandRequest brand = new BrandRequest("nvidea");

        Brand nvideaBrand = new Brand();
        nvideaBrand.setName(brand.name());
        nvideaBrand.setId(UUID.randomUUID());

        when(brandService.createBrand(any(brand.getClass()))).thenReturn(nvideaBrand);

        String request = objectMapper.writeValueAsString(brand);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nvidea"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void should_beAbleToGetAllBrands() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/brand"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.FeaturedResponseDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.featured.FeaturedServiceImpl;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class FeaturedProductControllerTest {

    @InjectMocks
    private FeaturedProductController featuredProductController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeaturedServiceImpl featuredService;

    @Mock
    private ProductRepository productRepository;


    @Test
    void getAllFeaturedProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/feature"))
                .andExpect(status().isOk());
    }

    @Test
    void addFeaturedProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        Category category = Category.builder().categoryName("GPU").build();
        Product product = Product.builder()
                .id(productId)
                .productName("Test")
                .productDescription("powerful gpu")
                .productPrice(20.00)
                .productAvailability(true)
                .inStock(10)
                .category(category)
                .createdAt(LocalDateTime.now())
                .productId("abcd")
                .build();

        when(featuredService.addFeaturedProduct(productId)).thenReturn(
                FeaturedResponseDto.builder().message("Now a featured Product").build());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/featured/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Now a featured Product"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void removeFeaturedProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        Category category = Category.builder().categoryName("GPU").build();
        Product product = Product.builder()
                .id(productId)
                .productName("Test")
                .productDescription("powerful gpu")
                .productPrice(20.00)
                .productAvailability(true)
                .inStock(10)
                .featured(true)
                .category(category)
                .createdAt(LocalDateTime.now())
                .productId("abcd")
                .build();

        when(featuredService.removeFeaturedProduct(productId)).thenReturn(
                FeaturedResponseDto.builder().message("Product is no more featured").build());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admin/featured/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Product is no more featured"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }
}
package com.amalitech.gpuconfigurator.controller;

import com.amalitech.gpuconfigurator.dto.product.CreateProductResponseDto;
import com.amalitech.gpuconfigurator.dto.product.ProductDto;
import com.amalitech.gpuconfigurator.dto.product.ProductResponse;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.model.Product;
import com.amalitech.gpuconfigurator.repository.ProductRepository;
import com.amalitech.gpuconfigurator.service.product.ProductServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;


    @Test
    @DisplayName("Test Success for creating new product")
    void addProduct() throws Exception {
        ProductDto requestDto = ProductDto.builder()
                .productName("Test")
                .productDescription("test server")
                .productPrice(100.00)
                .productId("1234")
                .category("SERVER")
                .build();

        List<MultipartFile> files = Arrays.asList(
                new MockMultipartFile("file1", "originalFilename1.txt", "text/plain", "File content 1".getBytes()),
                new MockMultipartFile("file2", "originalFilename2.txt", "text/plain", "File content 2".getBytes())
        );

        MultipartFile coverImage = new MockMultipartFile("coverImage", "originalFilename1.txt", "text/plain", "File content 1".getBytes());
        List<String> imageUrls = Arrays.asList(
                "http://example.com/image1.jpg",
                "http://example.com/image2.jpg"
        );

        CreateProductResponseDto expectedResponse = CreateProductResponseDto.builder()
                .productName("Test")
                .productDescription("test server")
                .productPrice(100.00)
                .productId("1234")
                .productCategory("SERVER")
                .productAvailability(true)
                .createdAt(LocalDateTime.now())
                .imageUrl(imageUrls)
                .coverImage("http://example.com/image1.jpg")
                .build();

        when(productService.createProduct(requestDto, files, coverImage)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/admin/product")
                        .file("file", files.get(0).getBytes())
                        .file("file", files.get(1).getBytes())
                        .file("coverImage", coverImage.getBytes())
                        .param("productName", requestDto.getProductName())
                        .param("productDescription", requestDto.getProductDescription())
                        .param("productPrice", String.valueOf(requestDto.getProductPrice()))
                        .param("productId", requestDto.getProductId())
                        .param("category", requestDto.getCategory()))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test success for getting all product")
    void getAllProducts() throws Exception {

        Category category = Category.builder().categoryName("GPU").build();
        List<Product> expectedProductList = Arrays.asList(
                Product.builder()
                        .id(UUID.randomUUID())
                        .productName("Product 1")
                        .productDescription("powerful gpu")
                        .productPrice(10.00)
                        .productAvailability(true)
                        .category(category)
                        .createdAt(LocalDateTime.now())
                        .productId("1234")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .productName("Product 2")
                        .productDescription("powerful gpu")
                        .productPrice(20.00)
                        .productAvailability(true)
                        .category(category)
                        .createdAt(LocalDateTime.now())
                        .productId("abcd")
                        .build()
        );

        Page<ProductResponse> expectedPage = new PageImpl<>(expectedProductList.stream()
                .map(product -> ProductResponse.builder()
                        .productName(product.getProductName())
                        .id(product.getId().toString())
                        .productId(product.getProductId())
                        .productDescription(product.getProductDescription())
                        .productPrice(BigDecimal.valueOf(product.getProductPrice()))
                        .imageUrl(product.getImageUrl())
                        .build())
                .collect(Collectors.toList()));

        when(productService.getAllProducts(anyInt(), anyInt(), anyString())).thenReturn(expectedPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/product")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "productName"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", Matchers.hasSize(expectedProductList.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productName").value("Product 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[0].productId").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].productName").value("Product 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products[1].productId").value("abcd"));

    }

}
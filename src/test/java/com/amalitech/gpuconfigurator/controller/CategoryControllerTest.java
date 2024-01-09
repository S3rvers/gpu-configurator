package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.category.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.category.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import com.amalitech.gpuconfigurator.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Test success for creating new category")
    void createCategory() throws Exception {
        CategoryRequestDto dto = new CategoryRequestDto("GPU");

        Category expectedCategory = Category.builder()
                .id(UUID.randomUUID())
                .categoryName(dto.name())
                .build();

        when(categoryService.createCategory(dto)).thenReturn(expectedCategory);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Test success for getting all categories")
    void getAllCategoriesTest() throws Exception {
        List<AllCategoryResponse> expectedCategories = Arrays.asList(
                AllCategoryResponse.builder().categoryName("SERVER").build(),
                AllCategoryResponse.builder().categoryName("GPU").build()
        );

        when(categoryService.getAllCategories()).thenReturn(expectedCategories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(expectedCategories.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryName").value("SERVER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryName").value("GPU"));
    }

    @Test
    @DisplayName("Test success for getting category by name")
    void getCategoryByNameTest() throws Exception {
        String categoryName = "GPU";
        Category expectedCategory = Category.builder()
                .id(UUID.fromString("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .categoryName(categoryName)
                .build();

        when(categoryService.getCategoryByName(categoryName)).thenReturn(expectedCategory);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/category/{name}", categoryName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName").value(categoryName));


    }
}


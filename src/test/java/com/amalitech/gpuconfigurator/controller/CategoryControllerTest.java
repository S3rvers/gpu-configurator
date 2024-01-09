package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.category.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.category.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.Category;

import com.amalitech.gpuconfigurator.service.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    @DisplayName("Test success for creating new category")
    void createCategory() {
        CategoryRequestDto dto = new CategoryRequestDto("GPU");
        Category expectedCategory = Category.builder()
                .id(UUID.fromString("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .categoryName(dto.name())
                .build();

        when(categoryService.createCategory(dto)).thenReturn(expectedCategory);
        Category response = categoryController.createCategory(dto);

        assertNotNull(response.getId());
        assertEquals(expectedCategory, response);
        assertEquals(HttpStatus.CREATED.value(), 201);
    }

    @Test
    @DisplayName("Test success for getting all categories")
    void getAllCategoriesTest() {
        List<AllCategoryResponse> expectedCategories = Arrays.asList(
                AllCategoryResponse.builder().name("SERVER").build(),
                AllCategoryResponse.builder().name("GPU").build()
        );

        when(categoryService.getAllCategories()).thenReturn(expectedCategories);
        List<AllCategoryResponse> response = categoryController.getAllCategories();


        assertEquals(expectedCategories.size(), response.size());
        assertEquals(expectedCategories, response);
        assertEquals(HttpStatus.OK.value(), 200);
    }

    @Test
    @DisplayName("Test success for getting category by name")
    void getCategoryByNameTest() {
        String categoryName = "GPU";
        Category expectedCategory = Category.builder()
                .id(UUID.fromString("6c4a87df-6831-4d49-a924-42979ec657ba"))
                .categoryName(categoryName)
                .build();

        when(categoryService.getCategoryByName(categoryName)).thenReturn(expectedCategory);
        Category response = categoryController.getCategoryByName(categoryName);

        assertEquals(expectedCategory.getCategoryName(), response.getCategoryName());
        assertEquals(expectedCategory, response);
        assertEquals(HttpStatus.OK.value(), 200);
    }
}


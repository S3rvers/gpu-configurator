package com.amalitech.gpuconfigurator.service;


import com.amalitech.gpuconfigurator.dto.category.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.category.CategoryRequestDto;
import com.amalitech.gpuconfigurator.exception.NotFoundException;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryRequestDto request) {
        var category = Category.builder().categoryName(request.name()).build();
        return categoryRepository.save(category);
    }




    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).orElseThrow(()-> new NotFoundException("Category not found"));
    }

    @Override
    public List<AllCategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        return allCategories.stream()
                .map(category -> AllCategoryResponse.builder().categoryName(category.getCategoryName()).build())
                .toList();

    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name).orElseThrow(()-> new NotFoundException(name+ " "+ "Not found"));
    }

}


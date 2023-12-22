package com.amalitech.gpuconfigurator.controller;


import com.amalitech.gpuconfigurator.dto.categoryconfig.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.categoryconfig.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.Category;
import com.amalitech.gpuconfigurator.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/v1/admin/category")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody CategoryRequestDto request){
        return categoryService.createCategory(request);
    }

    @GetMapping("/v1/admin/category")
    @ResponseStatus(HttpStatus.OK)
    public List<AllCategoryResponse> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/v1/admin/category/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategoryByName(@PathVariable("name") String name){
        return categoryService.getCategoryByName(name);
    }

}

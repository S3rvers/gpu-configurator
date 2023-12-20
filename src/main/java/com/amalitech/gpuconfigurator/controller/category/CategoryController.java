package com.amalitech.gpuconfigurator.controller.category;


import com.amalitech.gpuconfigurator.dto.AllCategoryResponse;
import com.amalitech.gpuconfigurator.dto.CategoryRequestDto;
import com.amalitech.gpuconfigurator.model.category.Category;
import com.amalitech.gpuconfigurator.service.CategoryService;
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

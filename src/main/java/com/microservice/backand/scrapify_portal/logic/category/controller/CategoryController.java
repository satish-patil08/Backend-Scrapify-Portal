package com.microservice.backand.scrapify_portal.logic.category.controller;

import com.microservice.backand.scrapify_portal.logic.category.entity.Category;
import com.microservice.backand.scrapify_portal.logic.category.service.CategoryService;
import com.microservice.backand.scrapify_portal.modelResponse.CategoryListStatusResponse;
import com.microservice.backand.scrapify_portal.modelResponse.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public StatusResponse createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @PutMapping("/update")
    public StatusResponse updateCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @GetMapping("/get-all")
    public CategoryListStatusResponse getAll() {
        return categoryService.getAll();
    }

    @DeleteMapping("/delete-by-id")
    public StatusResponse delete(Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}

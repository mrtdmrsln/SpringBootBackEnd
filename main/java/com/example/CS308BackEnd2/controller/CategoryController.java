package com.example.CS308BackEnd2.controller;


import com.example.CS308BackEnd2.model.BookDto;
import com.example.CS308BackEnd2.model.Category;
import com.example.CS308BackEnd2.model.Product;
import com.example.CS308BackEnd2.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(maxAge = 3600, allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition","Content-Type","Content-Length","Authorization","Set-Cookie","Cookie"},
        allowCredentials = "true",origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Category> createCategory(@RequestBody String categoryName){
        Category category = categoryService.createCategory(categoryName);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    /*
    @PostMapping("/createWithBooks")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, Object> categoryMap){
        Category category = categoryService.createCategoryWithBooks((String) categoryMap.get("categoryName")
                , (Integer) categoryMap.get("bookIdList"));
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    */
}

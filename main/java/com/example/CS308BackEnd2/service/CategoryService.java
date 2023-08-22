package com.example.CS308BackEnd2.service;

import com.example.CS308BackEnd2.model.Category;
import com.example.CS308BackEnd2.repository.BookRepository;
import com.example.CS308BackEnd2.repository.CategoryRepository;
import com.example.CS308BackEnd2.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;



    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Category createCategory(String name){
        Category category = new Category(name);
        categoryRepository.save(category);
        return category;
    }

    public Category createCategoryWithBooks(String name, List<Long> prodIds){
        Category category = categoryRepository.findByNameIsIgnoreCase(name);
        for(int i = 0; i < prodIds.size(); i++){
            category.addBook(bookRepository.findById(prodIds.get(i)).get());
        }
        categoryRepository.save(category);
        return category;
    }

}

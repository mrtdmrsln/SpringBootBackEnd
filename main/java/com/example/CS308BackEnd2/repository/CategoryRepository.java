package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    public Category findByNameIsIgnoreCase(String name);

}

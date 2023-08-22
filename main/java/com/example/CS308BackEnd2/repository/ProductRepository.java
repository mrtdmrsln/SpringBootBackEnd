package com.example.CS308BackEnd2.repository;



import com.example.CS308BackEnd2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product findFirstByOrderByIdDesc();

    public List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String search1, String search2);


}


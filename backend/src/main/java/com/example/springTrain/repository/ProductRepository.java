package com.example.springTrain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springTrain.enums.ProductCategory;
import com.example.springTrain.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query to find products by category
    List<Product> findByCategory(ProductCategory category);
}

package com.example.springTrain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springTrain.model.Cart;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
    Cart findByUserAndProduct(User user, Product product);
    void deleteByUser(User user);
    
    long countByUserId(Long userId);
    
    List<Cart> findByUser(User user);
    List<Cart> findByUserId(Long userId);
}

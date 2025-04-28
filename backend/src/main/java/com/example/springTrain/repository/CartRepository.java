package com.example.springTrain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springTrain.model.Cart;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	
	Optional<Cart> findByUserAndProduct(User user, Product product);
	List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
	void deleteByUser(User user);
	long countByUser(User user);
	int deleteByProductId(Long id);
}

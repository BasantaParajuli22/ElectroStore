package com.example.springTrain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query to find orders by customer ID
    List<Order> findByCustomerId(Long customerId);
}
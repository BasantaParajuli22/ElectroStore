package com.example.springTrain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    // Custom query to find orders by customer ID
//    List<Order> findByCustomer_Id(Long customerId);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :id")
    List<Order> findByCustomer_Id(@Param("id")Long id);

	Optional<Order> findByCustomerId(Long id);

	void deleteByCustomer_Id(Long customerId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findOrderItemsById(@Param("orderId") Long orderId);

	

}
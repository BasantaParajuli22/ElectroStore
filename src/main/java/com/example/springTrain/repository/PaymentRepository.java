package com.example.springTrain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

//	Optional<Payment> findByOrder_Id(Long orderId);

	List<Payment> findByOrder_Id(Long orderId);
}
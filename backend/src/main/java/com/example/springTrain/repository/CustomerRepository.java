package com.example.springTrain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query to find a customer by user ID
    Optional<Customer> findByUserId(Long userId);

    @Query("SELECT c.id FROM Customer c WHERE c.user.email = :email")
    Long findIdByUser_Email(@Param("email") String email);

	@Query("SELECT c FROM Customer c WHERE c.user.id = :id")
	Customer findByCustomer_Id(@Param("id")Long id);


}
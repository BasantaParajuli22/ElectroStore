package com.example.springTrain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find a user by email (optional)
    Optional<User> findByEmail(String email);
}
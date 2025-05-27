package com.example.springTrain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springTrain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

//	Long findIdByEmail(String email);

	@Query("SELECT u.id FROM User u WHERE u.email = :email")
	Optional<Long> findUserIdByEmail(@Param("email") String email);

	Optional<User> findById(Long userId);


}
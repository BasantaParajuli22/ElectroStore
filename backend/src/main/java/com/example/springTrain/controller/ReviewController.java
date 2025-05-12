package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.ReviewDto;
import com.example.springTrain.model.User;
import com.example.springTrain.service.ReviewService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService,
    		 UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
    	List<ReviewDto> reviewList = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) {
    	ReviewDto review = reviewService.getReviewById(id);
    	return ResponseEntity.ok(review);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<ReviewDto>> getReviewsByProductId(@PathVariable("id") Long productId) {
    	List<ReviewDto> reviewList = reviewService.getReviewsByProductId(productId);
    	return ResponseEntity.ok(reviewList);
    }
    
    
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto review) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName(); // Get the username (email) from the token
	    User user = userService.findByUserEmail(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    
        ReviewDto createdReview = reviewService.createReview(review);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("id") Long id,
    		@RequestBody ReviewDto review) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName(); // Get the username (email) from the token
	    User user = userService.findByUserEmail(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    
    	ReviewDto updatedReview = reviewService.updateReview(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String email = authentication.getName(); // Get the username (email) from the token
	    User user = userService.findByUserEmail(email);
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

}
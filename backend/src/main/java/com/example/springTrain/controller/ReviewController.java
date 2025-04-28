package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.ReviewDto;
import com.example.springTrain.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
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
        ReviewDto createdReview = reviewService.createReview(review);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable("id") Long id,
    		@RequestBody ReviewDto review) {
    	ReviewDto updatedReview = reviewService.updateReview(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

}
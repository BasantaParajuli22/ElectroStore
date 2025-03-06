package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.model.Review;
import com.example.springTrain.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Review createReview(Review review) {
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review reviewDetails) {
        Review review = getReviewById(id);
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
    	reviewRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Review not found"));
    	
        reviewRepository.deleteById(id);
    }

    public List<Review> getReviewsByProductId(Long productId) {
    	reviewRepository.findByProduct_Id(productId)
		.orElseThrow(() -> new RuntimeException("Review not found"));
    	
        return reviewRepository.findByProductId(productId);
    }
}

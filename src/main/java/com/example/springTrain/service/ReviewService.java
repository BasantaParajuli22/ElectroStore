package com.example.springTrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.ReviewDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.Review;
import com.example.springTrain.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public ReviewService(CustomerService customerService,
    		ProductService productService,
    		ReviewRepository reviewRepository) {
    	this.reviewRepository = reviewRepository;
        this.customerService = customerService;
        this.productService = productService;
    }
    
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Review createReview(ReviewDto reviewDto) {
    	
    	try {
    		Customer customer = customerService.getCustomerById(reviewDto.getCustomerId());
    		Product product = productService.getProductById(reviewDto.getProductId());
    		
    		Review review = new Review();
    		review.setCustomer(customer);
    		review.setProduct(product);
    		review.setComment(reviewDto.getComment());
    		review.setRating(reviewDto.getRating());
    		
    		return reviewRepository.save(review);
			
		} catch (Exception e) {
			throw new CreationFailedException("Review creation failed");
		}
    }

    public Review updateReview(Long id, ReviewDto reviewDto) {
    	
    	if(reviewDto == null || id == null) {
    		throw new IllegalArgumentException("reviewDto and id are null");
    	}
        Review review = getReviewById(id);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
    	Review review = reviewRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Review not found"));
    	
        reviewRepository.deleteById(id);
    }

    public List<Review> getReviewsByProductId(Long productId) {
    	List<Review> reviews = reviewRepository.findByProduct_Id(productId);
        return reviews;
    }
}

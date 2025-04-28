package com.example.springTrain.service;

import java.util.List;
import java.util.Optional;

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
    
    //review to responseDto conversion
    private ReviewDto convertToReviewResponseDto(Review review) {
    	ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        
        // Include related entity IDs/names if needed
        if (review.getProduct() != null) {
            dto.setProductId(review.getProduct().getId());
        }
        if (review.getCustomer() != null) {
            dto.setCustomerId(review.getCustomer().getId());
        }
        
        return dto;
    }
    
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()//stream all
        		.map(this::convertToReviewResponseDto)//map all responseDto to list
        		.toList();
    }

    public ReviewDto getReviewById(Long id) {
    	Review review= reviewRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Review not found"));
    	return convertToReviewResponseDto(review);
    	
    }

    public ReviewDto createReview(ReviewDto reviewDto) {
    	
    	try {
    		Customer customer = customerService.getCustomerById(reviewDto.getCustomerId());
    		Product product = productService.getProductById(reviewDto.getProductId());
    		
    		Review review = new Review();
    		review.setCustomer(customer);
    		review.setProduct(product);
    		review.setComment(reviewDto.getComment());
    		review.setRating(reviewDto.getRating());
    		reviewRepository.save(review);
    		
    		return convertToReviewResponseDto(review);
		} catch (Exception e) {
			throw new CreationFailedException("Review creation failed");
		}
    }

    public ReviewDto updateReview(Long id, ReviewDto reviewDto) {
    	
    	if(reviewDto == null || id == null) {
    		throw new IllegalArgumentException("reviewDto and id are null");
    	}
    	Customer customer = customerService.getCustomerById(reviewDto.getCustomerId());
		Product product = productService.getProductById(reviewDto.getProductId());
		
        Review review = reviewRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Review not found"));
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setCustomer(customer);
        review.setProduct(product);
        reviewRepository.save(review);
        
        return convertToReviewResponseDto(review);
    }

    public void deleteReview(Long id) {
    	Review review = reviewRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Review not found"));
    	
        reviewRepository.deleteById(id);
    }

    public List<ReviewDto> getReviewsByProductId(Long productId) {
    	List<ReviewDto> reviews = reviewRepository.findByProduct_Id(productId).stream()
    			.map(this::convertToReviewResponseDto)
    			.toList();
        return reviews;
    }
}

package com.example.springTrain.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.springTrain.dto.CartCountDTO;
import com.example.springTrain.dto.CartDto;
import com.example.springTrain.model.User;
import com.example.springTrain.service.CartService;
import com.example.springTrain.service.ProductService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/users/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService,
    		UserService userService,
    		ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody CartDto cartDto) {
	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token

       User user = userService.findByUserEmail(email);
       logger.info("Add to cart request for user: {}", email);
        try {

            CartDto cartItemDto = cartService.addToCart(cartDto, user);
            logger.info("Product {} added to cart for user {}", cartDto.getProductId(), email);
            return ResponseEntity.ok(cartItemDto);
        } catch (RuntimeException e) {
            logger.error("Error adding to cart: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getCartItems() {
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       
       User user = userService.findByUserEmail(email);
        try {
            List<CartDto> cartItems = cartService.getCartItems(user);
            return ResponseEntity.ok(cartItems);
            
        } catch (RuntimeException e) {
            logger.error("Error fetching cart items: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to fetch cart items"));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCartCount() {
    	
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       User user = userService.findByUserEmail(email);
        try {
            long count = cartService.getCartItemsCount(user);
            return ResponseEntity.ok(new CartCountDTO(count));       
        } catch (RuntimeException e) {
            logger.error("Error getting cart count: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable("cartItemId") Long cartItemId) {
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       User user = userService.findByUserEmail(email);
       try {
		    cartService.deleteCartItem(cartItemId, user);
		    return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting cart item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Item not found in cart"));
        }
    }

    
    @PutMapping
    public ResponseEntity<?> updateCartItem(
            @RequestBody CartDto cartDto) {
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
     
       User user = userService.findByUserEmail(email);
        try {
            CartDto updatedItem = cartService.updateQuantity(cartDto, user);
            return ResponseEntity.ok(updatedItem);

        } catch (RuntimeException e) {
            logger.error("Update failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
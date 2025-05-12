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

import com.example.springTrain.dto.AddToCartDTO;
import com.example.springTrain.dto.CartCountDTO;
import com.example.springTrain.dto.CartDTO;
import com.example.springTrain.model.Cart;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;
import com.example.springTrain.service.CartService;
import com.example.springTrain.service.ProductService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/users")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService,
    		UserService userService,
    		ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartDTO addToCartDTO) {
    	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String email = authentication.getName(); // Get the username (email) from the token
           
        logger.info("Add to cart request for user: {}", email);
        try {
            User user = userService.findByUserEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "User not found"));
            }

            Cart cartItem = cartService.addToCart(addToCartDTO, user);
            logger.info("Product {} added to cart for user {}", addToCartDTO.getProductId(), email);
            
            return ResponseEntity.ok(cartItem);
            
        } catch (RuntimeException e) {
            logger.error("Error adding to cart: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCartItems() {
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       
        try {
            User user = userService.findByUserEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Unauthorized access"));
            }

            List<CartDTO> cartItems = cartService.getCartItems(user);
            return ResponseEntity.ok(cartItems);
            
        } catch (RuntimeException e) {
            logger.error("Error fetching cart items: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to fetch cart items"));
        }
    }

    @GetMapping("/cart/count")
    public ResponseEntity<?> getCartCount() {
    	
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
        try {
            User user = userService.findByUserEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            long count = cartService.getCartItemsCount(user);
            return ResponseEntity.ok(new CartCountDTO(count));
            
        } catch (RuntimeException e) {
            logger.error("Error getting cart count: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/cart/delete/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable("cartItemId") Long cartItemId) {
 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       try {
            User user = userService.findByUserEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            cartService.deleteCartItem(cartItemId, user);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            logger.error("Error deleting cart item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Item not found in cart"));
        }
    }

    @PutMapping("/cart/update")
    public ResponseEntity<?> updateCartItem(
            @RequestBody AddToCartDTO updateDTO) {

 	   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName(); // Get the username (email) from the token
       
        try {
            User user = userService.findByUserEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Product product = productService.findById(updateDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
            Cart updatedItem = cartService.updateQuantity(updateDTO, user);
            return ResponseEntity.ok(updatedItem);

        } catch (RuntimeException e) {
            logger.error("Update failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
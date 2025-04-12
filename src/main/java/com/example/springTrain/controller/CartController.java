package com.example.springTrain.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.AddToCartDTO;
import com.example.springTrain.dto.CartCountDTO;
import com.example.springTrain.dto.CartDTO;
import com.example.springTrain.model.User;
import com.example.springTrain.service.CartService;
import com.example.springTrain.service.UserService;
import com.example.springTrain.util.JwtUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDTO addToCartDTO,
                                     @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.getUserEmailFromToken(token);
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            cartService.addToCart(addToCartDTO, user);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
    
    @GetMapping
    public ResponseEntity<List<CartDTO>> getCartItems(@RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        List<CartDTO> cartDTOList = cartService.listCartItems(user);
        return ResponseEntity.ok(cartDTOList);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id,
                                          @RequestHeader("Authorization") String token) {
        User user = userService.getUserFromToken(token);
        cartService.deleteCartItem(id, user);
        return ResponseEntity.ok().build();
    }
    
    
    @GetMapping("/count")
    public ResponseEntity<CartCountDTO> getCartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok(new CartCountDTO(0));
        }
        
        long count = cartService.getCartItemCount(user.getId());
        return ResponseEntity.ok(new CartCountDTO(count));
    }
}
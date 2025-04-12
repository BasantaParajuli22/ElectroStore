package com.example.springTrain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.AddToCartDTO;
import com.example.springTrain.dto.CartDTO;
import com.example.springTrain.model.Cart;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductService productService;
    
    public List<CartDTO> listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        return cartList.stream().map(cart -> {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setProductId(cart.getProduct().getId());
            cartDTO.setProductName(cart.getProduct().getName());
            cartDTO.setPrice(cart.getProduct().getPrice());
            cartDTO.setImageName(cart.getProduct().getImageName());
            cartDTO.setQuantity(cart.getQuantity());
            return cartDTO;
        }).collect(Collectors.toList());
    }
    
    public void addToCart(AddToCartDTO addToCartDTO, User user) {
        Product product = productService.getProductById(addToCartDTO.getProductId());
        
        Cart cart = cartRepository.findByUserAndProduct(user, product);
        
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + addToCartDTO.getQuantity());
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(addToCartDTO.getQuantity());
        }
        
        cartRepository.save(cart);
    }

    public long getCartItemCount(Long userId) {
        return cartRepository.countByUserId(userId);
    }
    
    // Alternative implementation if you want to sum quantities:
    public int getTotalCartItems(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream().mapToInt(Cart::getQuantity).sum();
    }
    
    public void deleteCartItem(Long cartItemId, User user) {
        cartRepository.findById(cartItemId).ifPresent(cart -> {
            if (cart.getUser().equals(user)) {
                cartRepository.delete(cart);
            }
        });
    }
    
    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}
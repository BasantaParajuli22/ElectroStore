package com.example.springTrain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.AddToCartDTO;
import com.example.springTrain.dto.CartDTO;
import com.example.springTrain.model.Cart;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.CartRepository;
import com.example.springTrain.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public long getCartItemsCount(User user) {
        return cartRepository.countByUser(user);
    }
    
    @Transactional
    public Cart addToCart(AddToCartDTO addToCartDTO, User user) {
        // Validate if the product is valid
        Product product = productRepository.findById(addToCartDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product already exists in the user's cart
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + addToCartDTO.getQuantity());
            return cartRepository.save(cart);  // <--- return updated cart
        } else {
            Cart cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(addToCartDTO.getQuantity());
            return cartRepository.save(cart);
        }
    }

    @Transactional
    public List<CartDTO> getCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartDTO> cartDTOList = new ArrayList<>();

        for (Cart cart : cartList) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cart.getId());
            cartDTO.setProductId(cart.getProduct().getId());
            cartDTO.setProductName(cart.getProduct().getName());
            cartDTO.setPrice(cart.getProduct().getPrice());
            cartDTO.setImageName(cart.getProduct().getImageName());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTOList.add(cartDTO);
        }
        return cartDTOList;
    }

    @Transactional
    public void deleteCartItem(Long cartItemId, User user) {
        // Validate if the cart item belongs to user
        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);

        if (optionalCart.isEmpty()) {
            throw new RuntimeException("Cart item not found");
        }

        Cart cart = optionalCart.get();

        if (cart.getUser() != user) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartRepository.delete(cart);
    }


    @Transactional
    public Cart updateQuantity(AddToCartDTO addToCartDTO, User user) {
        Product product = productRepository.findById(addToCartDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(addToCartDTO.getQuantity());
        return cartRepository.save(cart);
    }

    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}
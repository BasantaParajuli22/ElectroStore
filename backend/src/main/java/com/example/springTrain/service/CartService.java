package com.example.springTrain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.CartDto;
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
    
    private CartDto convertTCartDto(Cart cart) {
    	CartDto dto = new CartDto();
    	dto.setId(cart.getId());
    	dto.setImageName(cart.getProduct().getImageName());
    	dto.setPrice(cart.getProduct().getPrice());
    	dto.setProductName(cart.getProduct().getName());
    	dto.setProductId(cart.getProduct().getId());
        dto.setQuantity(cart.getQuantity());
        return dto;
    }
    
    @Transactional
    public CartDto addToCart(CartDto cartDto, User user) {
        // Validate if the product is valid
        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product already exists in the user's cart
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);
        if (existingCart.isPresent()) {
        	
            Cart cart = existingCart.get();//getting existing cart and adding new values
            long maxQuantity = cart.getProduct().getStockQuantity();
            
            //get previous Quantity -> cart.getQuantity() and add to new Quantity -> cartDto.getQuantity()
            cart.setQuantity(cart.getQuantity() + cartDto.getQuantity());
            //if more than max Quantity added throw error
            if(cart.getQuantity() > maxQuantity) {
            	 throw new RuntimeException("Max cart size exceeded");
            }
            Cart updatedCart = cartRepository.save(cart);
            return convertTCartDto(updatedCart);
        } else {
            Cart cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(cartDto.getQuantity());
            
            Cart newCart = cartRepository.save(cart);
            return convertTCartDto(newCart);
        }
    }
    


    @Transactional
    public List<CartDto> getCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartDto> cartDTOList = new ArrayList<>();

        for (Cart cart : cartList) {
            CartDto cartDTO = new CartDto();
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
    public CartDto updateQuantity(CartDto addToCartDTO, User user) {
    	
        Product product = productRepository.findById(addToCartDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found to update cart"));

        Cart cart = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Cart item not found to update cart"));

        cart.setQuantity(addToCartDTO.getQuantity());
        Cart updatedCart = cartRepository.save(cart);
        return convertTCartDto(updatedCart);
    }

    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }

}
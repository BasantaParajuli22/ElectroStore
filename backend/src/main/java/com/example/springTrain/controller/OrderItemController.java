package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.model.User;
import com.example.springTrain.service.OrderItemService;
import com.example.springTrain.service.OrderService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/orders-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderItemController(OrderItemService orderItemService,
    		OrderService orderService,
    		UserService userService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.userService = userService;
    }

    //add items to order and display all items
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> addItemsToOrder(@PathVariable Long orderId,
                                                    @RequestBody List<OrderItemDto> orderItemDto) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
   	 	Order order = orderService.getOrderById(orderId);	
        List<OrderItemDto> newItem = orderItemService.addItemsToOrder(order, orderItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    
    //add a item and display item
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{orderId}/item")
    public ResponseEntity<OrderItemDto> addItemToOrder(@PathVariable Long orderId,
                                                    @RequestBody OrderItemDto orderItemDto) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    	OrderItemDto newItem = orderItemService.addAItemToOrder(orderId, orderItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderItemDto> updateItemInOrder(@PathVariable("orderId") Long orderId,
                                                      @PathVariable("itemId") Long itemId,
                                                      @RequestBody OrderItem orderItem) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    	OrderItemDto updatedOrderItem = orderItemService.updateItemInOrder(orderId, itemId, orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("orderId") Long orderId,
                                                @PathVariable("itemId") Long itemId) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            orderItemService.deleteOrderItem(orderId, itemId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}

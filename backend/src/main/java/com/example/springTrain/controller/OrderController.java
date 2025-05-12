package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderRequest;
import com.example.springTrain.model.User;
import com.example.springTrain.service.OrderRequestService;
import com.example.springTrain.service.OrderService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRequestService orderRequestService;
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderRequestService orderRequestService,
    		OrderService orderService,
    		UserService userService) {
        this.orderRequestService = orderRequestService;
        this.orderService = orderService;
        this.userService = userService;
    }
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderDtoById(@PathVariable("id") Long id) {
        
		OrderDto order = orderService.getOrderDtoById(id);
		return ResponseEntity.ok(order);
    }
    
    //create order and add orderItem at once
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<String> createOrderAndOrderItem(@RequestBody OrderRequest request) {	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
		orderRequestService.createOrderAndOrderItem(request.getOrderDto(),
				request.getOrderItemDto());
		return ResponseEntity.ok("Order and OrdeItem Creation successful"); 
    }
    
    //only create order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/new")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }	
        
		orderService.createOrder(orderDto);
		return ResponseEntity.ok("Order Creation successfull");
    }
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id,
    		@RequestBody OrderDto orderDto) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    	OrderDto order = orderService.updateOrder(id, orderDto);
		 return ResponseEntity.ok(order); 
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username (email) from the token
        User user = userService.findByUserEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
	 	orderService.deleteOrder(id);
	 	return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable("customerId") Long customerId) {
    	List<OrderDto> orderList = orderService.getOrdersByCustomerId(customerId);
    	return ResponseEntity.ok(orderList); 
    }
}
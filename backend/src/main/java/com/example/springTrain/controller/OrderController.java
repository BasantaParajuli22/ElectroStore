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
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.User;
import com.example.springTrain.service.CustomerService;
import com.example.springTrain.service.OrderRequestService;
import com.example.springTrain.service.OrderService;
import com.example.springTrain.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRequestService orderRequestService;
    private final OrderService orderService;
    private final UserService userService;
    private final CustomerService customerService;

    public OrderController(OrderRequestService orderRequestService,
    		OrderService orderService,
    		UserService userService,
    		CustomerService customerService) {
        this.orderRequestService = orderRequestService;
        this.orderService = orderService;
        this.userService = userService;
        this.customerService =customerService;
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
    
    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> getMyOrders() {   	
        Long authenticatedUserId = userService.getAuthenticatedUserId();
        
        Customer customer = customerService.getCustomerByUserId(authenticatedUserId);
        List<OrderDto> orderDtos = orderService.getAllOrdersForCustomer(customer.getId());
        
        if (orderDtos.isEmpty()) {
            System.out.println("No orders found for customer ID: " + customer.getId());
        }
        return ResponseEntity.ok(orderDtos);
    }
    
    
    //create order and add orderItem at once
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createOrderAndOrderItem(@RequestBody OrderRequest request) {	
        Long authenticatedUserId = userService.getAuthenticatedUserId();

        User user = userService.getUserByUserId(authenticatedUserId);

		 try {
			 orderRequestService.createOrderAndOrderItem(request.getOrderDto(),
					 request.getOrderItemDto(),user);
			return ResponseEntity.ok("Order and OrdeItem Creation successful"); 
        }catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order and OrdeItem Creation Error: " +e.getMessage());
        }
    }
    
    //only create order
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/new")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        Long authenticatedUserId = userService.getAuthenticatedUserId();

    	User user = userService.getUserByUserId(authenticatedUserId);
        try {
        	orderService.createOrder(orderDto, user.getCustomer().getId());
        	return ResponseEntity.ok("Order Creation successfull");
        }catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("order creation error: " +e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id,
    		@RequestBody OrderDto orderDto) {
        Long authenticatedUserId = userService.getAuthenticatedUserId();

        try {
        	OrderDto order = orderService.updateOrder(id, orderDto);
   		 return ResponseEntity.ok(order);
        }catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("order creation error: " +e);
        }
    	 
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        Long authenticatedUserId = userService.getAuthenticatedUserId();

	 	orderService.deleteOrder(id);
	 	return ResponseEntity.noContent().build(); 
    }
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/my")
    public ResponseEntity<?> deleteMyOrders() {
        Long authenticatedUserId = userService.getAuthenticatedUserId();
    	User user = userService.getUserByUserId(authenticatedUserId);

	 	orderService.deleteAllOfMyOrders(user.getCustomer().getId());
	 	return ResponseEntity.noContent().build(); 
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteALLOrders() {
        Long authenticatedUserId = userService.getAuthenticatedUserId();  
        
	 	orderService.deleteAllOrders();
	 	return ResponseEntity.noContent().build(); 
    }
}
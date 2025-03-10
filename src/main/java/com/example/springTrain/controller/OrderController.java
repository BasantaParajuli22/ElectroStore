package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.example.springTrain.model.Order;
import com.example.springTrain.service.CustomerService;
import com.example.springTrain.service.OrderRequestService;
import com.example.springTrain.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRequestService orderRequestService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderRequestService orderRequestService,
    		OrderService orderService,
    		CustomerService customerService) {
        this.orderRequestService = orderRequestService;
        this.orderService = orderService;
        this.customerService = customerService;

    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id,
    		Authentication authentication) {
        
		Order order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
    }
    
    //create order and add orderItem 
    @PostMapping
    public ResponseEntity<String> createOrderAndOrderItem(@RequestBody OrderRequest request,
    		Authentication authentication) {
    	
		orderRequestService.createOrderAndOrderItem(request.getOrderDto(),
				request.getOrderItemDto());
		return ResponseEntity.ok("Order and OrdeItem Creation successfull"); 
    }
    
    //only create order
    @PostMapping("/new")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto,
    		Authentication authentication) {
    	
		orderService.createOrder(orderDto);
		return ResponseEntity.ok("Order Creation successfull");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        
		 Order order = orderService.updateOrder(id, orderDto);
		 return ResponseEntity.ok(order); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
 
	 	orderService.deleteOrder(id);
	 	return ResponseEntity.noContent().build(); 
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable Long customerId) {
    	List<Order> orderList = orderService.getOrdersByCustomerId(customerId);
    	return ResponseEntity.ok(orderList); 
    }
}
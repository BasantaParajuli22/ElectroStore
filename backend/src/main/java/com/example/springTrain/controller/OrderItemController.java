package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.model.Order;
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


    //to work on
    //items max quantity should not exceed product max quantity 
    //sum of items max quantity in a order should not exceed product max quantity 

    
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> viewAllItemsByOrderId(@PathVariable("orderId") Long orderId ) {
        boolean isValidRequest = userService.isCurrentUserOrderOwner(orderId);
        if(!isValidRequest) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not same userId to get order items");
        }

        List<OrderItemDto> newItem = orderItemService.viewAllItemsofOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(newItem);
    } 
    
    //add items to order and display all items
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<?> addItemsToOrder(@PathVariable("orderId") Long orderId,
                                                    @RequestBody List<OrderItemDto> orderItemDto) {
    	boolean isValidRequest = userService.isCurrentUserOrderOwner(orderId);
        if(!isValidRequest) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not same userId to get order items");
        }
        
        Order order = orderService.getOrderById(orderId);
        List<OrderItemDto> newItem = orderItemService.addItemsToOrder(order, orderItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    
    //add a item and display item
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/{orderId}/item")
    public ResponseEntity<?> addItemToOrder(@PathVariable("orderId") Long orderId,
                                                    @RequestBody OrderItemDto orderItemDto) {
    	boolean isValidRequest = userService.isCurrentUserOrderOwner(orderId);
        if(!isValidRequest) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not same userId to get order items");
        }
    	OrderItemDto newItem = orderItemService.addAItemToOrder(orderId, orderItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<?> updateItemInOrder(@PathVariable("orderId") Long orderId,
                                                      @PathVariable("itemId") Long itemId,
                                                      @RequestBody OrderItemDto orderItem) {
    	boolean isValidRequest = userService.isCurrentUserOrderOwner(orderId);
        if(!isValidRequest) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not same userId to get order items");
        }
       
    	OrderItemDto updatedOrderItem = orderItemService.updateItemInOrder(orderId, itemId, orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable("orderId") Long orderId,
                                                @PathVariable("itemId") Long itemId) {
    	boolean isValidRequest = userService.isCurrentUserOrderOwner(orderId);
        if(!isValidRequest) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not same userId to get order items");
        }
       
        orderItemService.deleteOrderItem(orderId, itemId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

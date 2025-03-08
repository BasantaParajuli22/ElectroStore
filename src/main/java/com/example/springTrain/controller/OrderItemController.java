package com.example.springTrain.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.springTrain.service.OrderItemService;
import com.example.springTrain.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public OrderItemController(OrderItemService orderItemService,
    		OrderService orderService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    //add items to order and display all items
    @PostMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> addItemsToOrder(@PathVariable Long orderId,
                                                    @RequestBody List<OrderItemDto> orderItemDto) {
   	 	Order order = orderService.getOrderById(orderId);	
        List<OrderItem> newItem = orderItemService.addItemsToOrder(order, orderItemDto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    
    //add a item and display item
    @PostMapping("/{orderId}/item")
    public ResponseEntity<OrderItem> addItemToOrder(@PathVariable Long orderId,
                                                    @RequestBody OrderItemDto orderItemDto) {
        OrderItem newItem = orderItemService.addAItemToOrder(orderId, orderItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }
    
 

    @PutMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderItem> updateItemInOrder(@PathVariable Long orderId,
                                                      @PathVariable Long itemId,
                                                      @RequestBody OrderItem orderItem) {
        OrderItem updatedOrderItem = orderItemService.updateItemInOrder(orderId, itemId, orderItem);
        return ResponseEntity.ok(updatedOrderItem);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderId,
                                                @PathVariable Long itemId) {
        try {
            orderItemService.deleteOrderItem(orderId, itemId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}

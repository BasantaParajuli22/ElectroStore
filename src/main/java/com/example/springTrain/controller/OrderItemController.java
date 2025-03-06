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

import com.example.springTrain.model.OrderItem;
import com.example.springTrain.service.OrderItemService;

@RestController
@RequestMapping("/api/orders")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> addItemToOrder(@PathVariable Long orderId,
                                                    @RequestBody OrderItem orderItem) {
    	
    	//add a item and display all items in that order
        List<OrderItem> newItem = orderItemService.addItemToOrder(orderId, orderItem);
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

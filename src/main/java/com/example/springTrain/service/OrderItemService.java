package com.example.springTrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.repository.OrderItemRepository;
import com.example.springTrain.repository.OrderRepository;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemService(OrderItemRepository orderItemRepository,
    		OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderItem> addItemToOrder(Long orderId, OrderItem orderItem) {
        // Validate input
        if (orderId == null || orderItem == null) {
            throw new IllegalArgumentException("OrderId or OrderItem cannot be null");
        }

        // Fetch the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Associate the orderItem with the order
        orderItem.setOrder(order);
        // Save the orderItem
        OrderItem savedItem = orderItemRepository.save(orderItem);

        // Fetch and return all items for the order
        return orderItemRepository.findAllByOrder_Id(orderId);
    }

    public OrderItem updateItemInOrder(Long orderId, Long itemId, OrderItem orderItem) {
        // Validate input
        if (orderId == null || itemId == null || orderItem == null) {
            throw new IllegalArgumentException("OrderId, ItemId, or OrderItem cannot be null");
        }

        // Fetch the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Fetch the existing orderItem
        OrderItem existingItem = orderItemRepository.findByOrder_IdAndId(orderId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        // Update the existing item
        existingItem.setQuantity(orderItem.getQuantity());
        existingItem.setProduct(orderItem.getProduct());
        // Update other fields as needed

        // Save the updated item
        return orderItemRepository.save(existingItem);
    }

    public void deleteOrderItem(Long orderId, Long itemId) {
        // Validate input
        if (orderId == null || itemId == null) {
            throw new IllegalArgumentException("OrderId or ItemId cannot be null");
        }

        // Fetch the orderItem
        OrderItem item = orderItemRepository.findByOrder_IdAndId(orderId, itemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        // Delete the item
        orderItemRepository.delete(item);
    }
}
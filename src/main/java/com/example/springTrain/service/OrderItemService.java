package com.example.springTrain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderItemDto;
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

    //to add list of orderItem to an order
    public List<OrderItem> addItemsToOrder(Order order, List<OrderItemDto> orderItemDto) {
        if (order == null || orderItemDto == null) {
            throw new IllegalArgumentException("order or OrderItem cannot be null");
        }
        
        List<OrderItem> orderItemList= new ArrayList<>();
        for( OrderItemDto orderItem:  orderItemDto) {  	
        	OrderItem newOrderItem = new OrderItem();
        	newOrderItem.setOrder(order);     
        	newOrderItem.setPrice(orderItem.getPrice());
        	newOrderItem.setQuantity(orderItem.getQuantity());
        	orderItemRepository.save(newOrderItem);
        	
        	orderItemList.add(newOrderItem);
        }
        	return orderItemList;
    }

    //to add a orderItem to an order using orderId
    public OrderItem addAItemToOrder(Long orderId, OrderItemDto orderItemDto) {
    	 if (orderId == null || orderItemDto == null) {
             throw new IllegalArgumentException("orderId or OrderItem cannot be null");
         }
    	 Order order = orderRepository.findById(orderId)
    	 .orElseThrow( () -> new IllegalArgumentException("order cannot be found by orderId "+orderId));
    	 
    	OrderItem newOrderItem = new OrderItem();
    	newOrderItem.setOrder(order);     
    	newOrderItem.setPrice(orderItemDto.getPrice());
    	newOrderItem.setQuantity(orderItemDto.getQuantity());
    	orderItemRepository.save(newOrderItem);
    	return newOrderItem;
    	
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
package com.example.springTrain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.DeleteFailedException;
import com.example.springTrain.exceptions.UpdateFailedException;
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
        
    	try {	
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
		} catch (Exception e) {
            throw new CreationFailedException("addItemsToOrder Failed " + e.getMessage());

		}
    }

    //to add a orderItem to an order using orderId
    public OrderItem addAItemToOrder(Long orderId, OrderItemDto orderItemDto) {
    	try {
			
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
		} catch (Exception e) {
            throw new CreationFailedException("addItemToOrder Failed " + e.getMessage());

		}
    	
    }
      
    public OrderItem updateItemInOrder(Long orderId, Long itemId, OrderItem orderItem) {
        try {
			
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
        	
        	existingItem.setQuantity(orderItem.getQuantity());
        	existingItem.setProduct(orderItem.getProduct());
        	
        	return orderItemRepository.save(existingItem);
		} catch (Exception e) {
            throw new UpdateFailedException("updateItemInOrder Failed " + e.getMessage());

		}
    }


    public void deleteOrderItem(Long orderId, Long itemId) {

    	try {
    		if (orderId == null || itemId == null) {
    			throw new IllegalArgumentException("OrderId or ItemId cannot be null");
    		}
    		
    		// Fetch the orderItem
    		OrderItem item = orderItemRepository.findByOrder_IdAndId(orderId, itemId)
    				.orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
    		// Delete the item
    		orderItemRepository.delete(item);
			
		} catch (Exception e) {
            throw new DeleteFailedException("deleteOrderItem Failed " + e.getMessage());

		}
    }



}
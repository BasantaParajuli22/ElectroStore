package com.example.springTrain.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.DeleteFailedException;
import com.example.springTrain.exceptions.UpdateFailedException;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.repository.OrderItemRepository;
import com.example.springTrain.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderItemService {
	
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    
    public OrderItemService(OrderItemRepository orderItemRepository,
    		OrderRepository orderRepository,
    		ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    private OrderItemDto convertToOrderItemResponseDto(OrderItem orderItem) {
    	OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());
        dto.setOrderId(orderItem.getOrder().getId());
        
        // Include product information if needed
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
        }
        
        return dto;
    }
    
    @Transactional
    public List<OrderItemDto> addItemsToOrder(Order order, List<OrderItemDto> orderItemDto) {
        
    	try {	
    		if (order == null || orderItemDto == null) {
    			throw new IllegalArgumentException("order or OrderItem cannot be null");
    		}
    		
		return orderItemDto.stream()
            .map(dto -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setPrice(dto.getPrice());
                orderItem.setQuantity(dto.getQuantity());
                orderItem.setProduct(productService.getProductById(dto.getProductId()));//set product in orderItem
                return orderItemRepository.save(orderItem);
            })
            .map(this::convertToOrderItemResponseDto)
            .collect(Collectors.toList());

		} catch (Exception e) {
            throw new CreationFailedException("addItemsToOrder Failed " + e.getMessage());

		}
    }

    @Transactional
    public OrderItemDto addAItemToOrder(Long orderId, OrderItemDto orderItemDto) {
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
    		
    		OrderItem savedItem = orderItemRepository.save(newOrderItem);
            return convertToOrderItemResponseDto(savedItem);
		} catch (Exception e) {
            throw new CreationFailedException("addItemToOrder Failed " + e.getMessage());

		}
    	
    }
    
    @Transactional
    public OrderItemDto updateItemInOrder(Long orderId, Long itemId, OrderItem orderItem) {
        try {
			
        	// Validate input
        	if (orderId == null || itemId == null || orderItem == null) {
        		throw new IllegalArgumentException("OrderId, ItemId, or OrderItem cannot be null");
        	}
        	
        	// Fetch the existing orderItem
        	OrderItem existingItem = orderItemRepository.findByOrder_IdAndId(orderId, itemId)
        			.orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
        	
        	existingItem.setQuantity(orderItem.getQuantity());
        	existingItem.setProduct(orderItem.getProduct());
        	
        	OrderItem updatedItem = orderItemRepository.save(existingItem);
            return convertToOrderItemResponseDto(updatedItem);
		} catch (Exception e) {
            throw new UpdateFailedException("updateItemInOrder Failed " + e.getMessage());

		}
    }

    @Transactional
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
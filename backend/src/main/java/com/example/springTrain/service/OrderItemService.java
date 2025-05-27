package com.example.springTrain.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.DeleteFailedException;
import com.example.springTrain.exceptions.UpdateFailedException;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.model.Product;
import com.example.springTrain.repository.OrderItemRepository;
import com.example.springTrain.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderItemService {

    private final OrderService orderService;
	
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    
    public OrderItemService(OrderItemRepository orderItemRepository,
    		OrderRepository orderRepository,
    		ProductService productService, OrderService orderService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderService = orderService;
    }

    
    
    @Transactional
    public List<OrderItemDto> addItemsToOrder(Order order, List<OrderItemDto> orderItemDto) {
        
    	try {	
    		if (order == null || orderItemDto == null) {
    			throw new IllegalArgumentException("order or OrderItem cannot be null");
    		}
    		
		return orderItemDto.stream()
            .map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                boolean isQValid =isQuantityValid(order.getId(), item);
        		if(!isQValid) {
        			throw new CreationFailedException("Not suitable quantity placed ");
        		}
        		orderItem.setQuantity(item.getQuantity());
                orderItem.setProduct(productService.getProductById(item.getProductId()));//set product in orderItem
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
    		
    		boolean isQValid =isQuantityValid(orderId, orderItemDto);
    		if(!isQValid) {
    			throw new RuntimeException("Not suitable quantity placed ");
    		}

        	Order order = orderService.getOrderById(orderId);
        	
    		OrderItem newOrderItem = new OrderItem();
    		newOrderItem.setOrder(order);     
    		newOrderItem.setQuantity(orderItemDto.getQuantity());
    		
    		OrderItem savedItem = orderItemRepository.save(newOrderItem);
            return convertToOrderItemResponseDto(savedItem);
		} catch (Exception e) {
            throw new CreationFailedException("addItemToOrder Failed " + e.getMessage());

		}
    }
    
    
	@Transactional
    public OrderItemDto updateItemInOrder(Long orderId, Long itemId, OrderItemDto orderItemDto) {
        try {	
        	// Validate input
        	if (orderId == null || itemId == null || orderItemDto == null) {
        		throw new IllegalArgumentException("OrderId, ItemId, or OrderItem cannot be null");
        	}
        	// Fetch the existing orderItem
        	OrderItem existingItem = getOrderItemFromOrder_IdAndItemId(orderId, itemId);
        	
        	boolean isQValid =isQuantityValid(orderId, orderItemDto);
    		if(!isQValid) {
    			throw new RuntimeException("Not suitable quantity placed ");
    		}
    		
        	existingItem.setQuantity(orderItemDto.getQuantity());
        	
        	OrderItem updatedItem = orderItemRepository.save(existingItem);
            return convertToOrderItemResponseDto(updatedItem);
		} catch (Exception e) {
            throw new UpdateFailedException("updateItemInOrder Failed " + e.getMessage());

		}
    }

    
	@Transactional
    public void deleteOrderItem(Long orderId, Long itemId) {
		if (orderId == null || itemId == null) {
			throw new IllegalArgumentException("OrderId or ItemId cannot be null");
		}
    	try {
    		// Fetch the orderItem
    		OrderItem item = getOrderItemFromOrder_IdAndItemId(orderId, itemId);
    		
    		// Delete the item
    		orderItemRepository.delete(item);
		} catch (Exception e) {
            throw new DeleteFailedException("deleteOrderItem Failed " + e.getMessage());

		}
    }

	public List<OrderItemDto> viewAllItemsofOrder(Long orderId) {
		List<OrderItem> items = orderRepository.findOrderItemsById(orderId);

		return items.stream()
			 .map(orderService::convertToOrderItemDto)
			 .toList();
	}

	private OrderItem getOrderItemFromOrder_IdAndItemId(Long orderId, Long itemId) {
    	OrderItem existingItem = orderItemRepository.findByOrder_IdAndId(orderId, itemId)
    			.orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
		return existingItem;
    	
	}

	//helper method
    private boolean isQuantityValid(Long orderId, OrderItemDto orderItemDto) {
		
		Long productId = orderItemDto.getProductId();
		Product existingProduct =productService.getProductById(productId);
		
		int existingStock = existingProduct.getStockQuantity();
		if(orderItemDto.getQuantity() <= 0) {
			  throw new RuntimeException("Cannot place Order for 0 quantity of  " + existingProduct.getName());
		}
		if(existingStock <= 0 || existingStock < orderItemDto.getQuantity()) {
			  throw new RuntimeException("Insufficient stock for product: " + existingProduct.getName());
		}
		return true;
	}
    
    //convert OrderItem to OrderItemDto method
    private OrderItemDto convertToOrderItemResponseDto(OrderItem orderItem) {
    	OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setPrice(orderItem.getPrice());
        dto.setQuantity(orderItem.getQuantity());
        dto.setOrderId(orderItem.getOrder().getId());
        
        // Include product information if needed
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getName());
        }
        
        return dto;
    }
}
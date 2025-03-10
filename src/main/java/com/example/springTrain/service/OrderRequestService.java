package com.example.springTrain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.dto.OrderRequest;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.model.Product;

import jakarta.transaction.Transactional;

@Service
public class OrderRequestService {

	Logger logger = LoggerFactory.getLogger(OrderRequestService.class);
	
	private final OrderService orderService; 
    private final OrderItemService orderItemService;
    private final ProductService productService;
    public OrderRequestService(OrderService orderService,
    		OrderItemService orderItemService,
    		ProductService productService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
    }
  
    // to create a order and add orderItems to order 
	// check if orderItem exists in product and if stockquantity is available
	@Transactional
	public ResponseEntity<OrderRequest> createOrderAndOrderItem(OrderDto orderDto,
			List<OrderItemDto> orderItemDtoList) {
		
		try {
			//create order which is to be saved in orderItem
			Order newOrder = orderService.createOrder(orderDto);
			if(orderDto == null){
				  throw new IllegalArgumentException("orderDto null when creating orderItem ");	
			}
			if(orderItemDtoList.isEmpty()) {
				  throw new IllegalArgumentException("orderItemDtoList is null or empty cannot add orderItem");	
			}
			//check if all the product exists and 
			//check if orderItem is available in product stock quantity
			for(OrderItemDto orderItemDto : orderItemDtoList) {
				
				Long productId = orderItemDto.getProductId();
				Product existingProduct =productService.getProductById(productId);
				
				if(existingProduct == null){
					  throw new IllegalArgumentException("Product not found with ID: " + productId);	
				}
				int stock = existingProduct.getStockQuantity();
				if(stock <= 0 || stock < orderItemDto.getQuantity()) {
					  throw new IllegalArgumentException("Insufficient stock for product: " + existingProduct.getName());
				}
			}//end of for loop
			
			//add orderItems and set order
			List<OrderItem> newOrderItemDto = orderItemService.addItemsToOrder(newOrder,
					orderItemDtoList);
			
			// Return the response
	        OrderRequest orderRequest = new OrderRequest();
	        orderRequest.setOrderDto(orderDto);
	        orderRequest.setOrderItemDto(orderItemDtoList);
	        return ResponseEntity.ok(orderRequest);
		} catch (Exception e) {	
			 logger.error("error caught while creating order "+e);
			 return ResponseEntity.badRequest().build();
		}	
	}
  
	
}

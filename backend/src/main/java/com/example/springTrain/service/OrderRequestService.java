package com.example.springTrain.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.dto.OrderRequest;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.Product;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderRequestService {

	Logger logger = LoggerFactory.getLogger(OrderRequestService.class);
	
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;

    public OrderRequestService(OrderItemService orderItemService,
    		ProductService productService,
    		CustomerService customerService,
    		OrderRepository orderRepository) {
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.customerService = customerService;
        this.orderRepository =orderRepository;
    }
  
    // to create a order and add orderItems to order 
	// check if orderItem exists in product and if stockquantity is available
	@Transactional
	public OrderRequest  createOrderAndOrderItem(OrderDto orderDto,
			List<OrderItemDto> orderItemDtoList, User user) {
		
		if(orderDto == null){
			  throw new IllegalArgumentException("orderDto null when creating orderItem ");	
		}
		if(orderItemDtoList.isEmpty()) {
			  throw new IllegalArgumentException("orderItemDtoList is null or empty cannot add orderItem");	
		}
		
		Order newOrder = null;;
		try {
			//create order which is to be saved in orderItem
			Order order = new Order();
			Customer customer = customerService.getCustomerById(user.getCustomer().getId());    		
			order.setCustomer(customer);//customer set
		    order.setPaymentStatus(orderDto.getPaymentStatus());
		    order.setOrderStatus(orderDto.getOrderStatus());
		    order.setDeliveryStatus(orderDto.getDeliveryStatus());
		    
		    newOrder = orderRepository.save(order);	       
		} catch (Exception e) {
		    throw new CreationFailedException("Order creation failed: " + e.getMessage());
		}
		//check if all the product exists and 
		//check if orderItem is available in product stock quantity
		for(OrderItemDto orderItemDto : orderItemDtoList) {
			
			Long productId = orderItemDto.getProductId();
			Product existingProduct =productService.getProductById(productId);
			
			if(existingProduct == null){
				  throw new IllegalArgumentException("Product not found with ID: " + productId);	
			}
			int existingStock = existingProduct.getStockQuantity();
			if(orderItemDto.getQuantity() <= 0) {
				  throw new IllegalArgumentException("Cannot place Order for 0 quantity of  " + existingProduct.getName());
			}
			if(existingStock <= 0 || existingStock < orderItemDto.getQuantity()) {
				  throw new IllegalArgumentException("Insufficient stock for product: " + existingProduct.getName());
			}
		}//end of for loop
		
		//add orderItems and set order
		List<OrderItemDto> newOrderItemDto = orderItemService.addItemsToOrder(newOrder,
				orderItemDtoList);
		
		// Return the response
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderDto(orderDto);
        orderRequest.setOrderItemDto(newOrderItemDto);
        return orderRequest;
			
	}
  
	
}

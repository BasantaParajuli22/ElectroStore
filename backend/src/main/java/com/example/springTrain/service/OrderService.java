package com.example.springTrain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.dto.OrderItemDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.OrderItem;
import com.example.springTrain.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final CustomerService customerService;
    private final OrderRepository orderRepository;

    public OrderService(CustomerService customerService,
    		OrderRepository orderRepository) {
        this.customerService = customerService;
        this.orderRepository = orderRepository;
    }
    
    private OrderDto convertToOrderResponseDto(Order order) {
    	OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setDeliveryStatus(order.getDeliveryStatus());
        dto.setCustomerId(order.getCustomer().getId());
        
     // Map each OrderItem to an OrderItemDto
        if (order.getOrderItems() != null) { 
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(this::convertToOrderItemDto) // Call a helper method to map individual items
                .toList();
            dto.setOrderItems(orderItemDtos);
        } else {
            // If order.getOrderItems() is null, set an empty list
            dto.setOrderItems(new ArrayList<>());
        }   
        return dto;
    }
    
    // Helper method to convert an OrderItem entity to an OrderItemDto
    public OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setId(orderItem.getId());
        itemDto.setQuantity(orderItem.getQuantity());
        itemDto.setPrice(orderItem.getPrice());

        // Safely get product details from the OrderItem's associated Product entity
        if (orderItem.getProduct() != null) {
            itemDto.setProductId(orderItem.getProduct().getId());
            itemDto.setProductName(orderItem.getProduct().getName());
        } else {
            itemDto.setProductId(null);
        }
        return itemDto;
    }
    
    public List<OrderDto> getAllOrders() {
    	return orderRepository.findAll().stream()
                .map(this::convertToOrderResponseDto)
                .toList();
    }

    public Order getOrderById(Long id) {
    	Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return order;
    }

    public OrderDto getOrderDtoById(Long id) {
    	Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToOrderResponseDto(order);
    }
    
    public List<OrderDto> getAllOrdersForCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomer_Id(customerId);
        return orders.stream()
                .map(this::convertToOrderResponseDto)
                .toList();
    }
    
//    @Transactional
//    public OrderDto createOrderAndReturnOrderDto(OrderDto orderDto,Long customerId) {
//    	if(orderDto == null) {
//    		throw new RuntimeException("orderDto cannot be null");
//    	}
//    	try {
//    	Customer customer = customerService.getCustomerById(customerId);    		
//    	Order order = new Order();
//    	order.setCustomer(customer);//customer set
//        order.setPaymentStatus(orderDto.getPaymentStatus());
//        order.setOrderStatus(orderDto.getOrderStatus());
//        order.setDeliveryStatus(orderDto.getDeliveryStatus());
//        
//        Order savedOrder = orderRepository.save(order);
//        return convertToOrderResponseDto(savedOrder);
//	    } catch (Exception e) {
//	        throw new CreationFailedException("Order creation failed: " + e.getMessage());
//	    }	
//    }
    
    @Transactional
    public Order createOrder(OrderDto orderDto,Long customerId) {
    	if(orderDto == null) {
    		throw new RuntimeException("orderDto cannot be null");
    	}
    	if(orderDto.getCustomerId() != customerId) {
    		throw new RuntimeException("not same customerID");
    	}
    	try {
    	Customer customer = customerService.getCustomerById(customerId);    		
    	Order order = new Order();
    	order.setCustomer(customer);//customer set
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setDeliveryStatus(orderDto.getDeliveryStatus());
        
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
	    } catch (Exception e) {
	        throw new CreationFailedException("Order creation failed: " + e.getMessage());
	    }	
    }
    
    @Transactional
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
    	if(id == null || orderDto == null) {
    		throw new IllegalArgumentException("id or orderDto cannot be null ");	
    	}
    	orderRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Order not found"));
    	
        Order order = getOrderById(id);
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setDeliveryStatus(orderDto.getDeliveryStatus());
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
    	if(id == null) {
    		throw new IllegalArgumentException("id cannot be null ");	
    	}
    	orderRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Order not found"));
    	
        orderRepository.deleteById(id);
    }

    @Transactional
	public void deleteAllOfMyOrders(Long customerId) {
        orderRepository.deleteByCustomer_Id(customerId);
	}
    
    public List<OrderDto> getOrderDtoByCustomerId(Long customerId) {
    	if(customerId == null) {
    		throw new IllegalArgumentException("customerId cannot be null ");	
    	}
    	try {
    		return orderRepository.findByCustomer_Id(customerId).stream()
    				.map(this::convertToOrderResponseDto)
    	            .toList();
		} catch (Exception e) {
			throw new RuntimeException("Orders not found by customerId" +e);
		}
    }

	public void deleteAllOrders() {
		try {
			orderRepository.deleteAll();
		} catch (Exception e) {
			throw new RuntimeException("Error while deleting orders by UserId" +e);
		}
	}




    
}
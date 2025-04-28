package com.example.springTrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
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
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setDeliveryStatus(order.getDeliveryStatus());
        
        // Include customer information if needed
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
        }
        
        return dto;
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
    
    @Transactional
    public OrderDto createOrderAndReturnOrderDto(OrderDto orderDto) {
    	if(orderDto == null) {
    		throw new RuntimeException("orderDto cannot be null");
    	}
    	try {
    	Customer customer = customerService.getCustomerById(orderDto.getCustomerId());    		
    	Order order = new Order();
    	order.setCustomer(customer);//customer set
    	order.setTotalAmount(orderDto.getTotalAmount());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setDeliveryStatus(orderDto.getDeliveryStatus());
        
        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
	    } catch (Exception e) {
	        throw new CreationFailedException("Order creation failed: " + e.getMessage());
	    }	
    }
    
    @Transactional
    public Order createOrder(OrderDto orderDto) {
    	if(orderDto == null) {
    		throw new RuntimeException("orderDto cannot be null");
    	}
    	try {
    	Customer customer = customerService.getCustomerById(orderDto.getCustomerId());    		
    	Order order = new Order();
    	order.setCustomer(customer);//customer set
    	order.setTotalAmount(orderDto.getTotalAmount());
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
        order.setTotalAmount(orderDto.getTotalAmount());
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

    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
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
    
}
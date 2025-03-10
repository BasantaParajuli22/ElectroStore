package com.example.springTrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
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
    
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order createOrder(OrderDto orderDto) {
    	if(orderDto == null) {
    		throw new RuntimeException("orderDto cannot be null");
    	}
    	Customer customer = customerService.getCustomerById(orderDto.getCustomerId());
    		
    	Order order = new Order();
    	order.setCustomer(customer);
    	order.setTotalAmount(orderDto.getTotalAmount());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setDeliveryStatus(orderDto.getDeliveryStatus());
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updateOrder(Long id, OrderDto orderDto) {
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
        return orderRepository.save(order);
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

    public List<Order> getOrdersByCustomerId(Long customerId) {
    	if(customerId == null) {
    		throw new IllegalArgumentException("customerId cannot be null ");	
    	}
    	try {
    		return orderRepository.findByCustomer_Id(customerId);
		} catch (Exception e) {
			throw new RuntimeException("Orders not found by customerId" +e);
		}
    }
    
}
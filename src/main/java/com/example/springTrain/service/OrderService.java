package com.example.springTrain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springTrain.dto.OrderDto;
import com.example.springTrain.enums.DeliveryStatus;
import com.example.springTrain.enums.OrderStatus;
import com.example.springTrain.enums.PaymentStatus;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.repository.CustomerRepository;
import com.example.springTrain.repository.OrderItemRepository;
import com.example.springTrain.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository,
    		OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
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
    	Customer customer = customerRepository.findById(orderDto.getCustomerId())
    			.orElseThrow(() -> new RuntimeException("Order not found"));
    	
    	Order order = new Order();
    	order.setCustomer(customer);
    	order.setTotalAmount(orderDto.getTotalAmount());
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryStatus(DeliveryStatus.NOT_SHIPPED);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updateOrder(Long id, OrderDto orderDto) {
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
    	orderRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Order not found"));
    	
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
    	try {
    		return orderRepository.findAllByCustomer_Id(customerId);
		} catch (Exception e) {
			throw new RuntimeException("Orders not found by customerId" +e);
		}
    }
    
}
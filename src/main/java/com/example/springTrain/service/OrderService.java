package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.enums.DeliveryStatus;
import com.example.springTrain.enums.OrderStatus;
import com.example.springTrain.enums.PaymentStatus;
import com.example.springTrain.model.Order;
import com.example.springTrain.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
        		.orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
    	
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setDeliveryStatus(DeliveryStatus.NOT_SHIPPED);
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
    	orderRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Order not found"));
    	
        Order order = getOrderById(id);
        order.setTotalAmount(orderDetails.getTotalAmount());
        order.setPaymentStatus(orderDetails.getPaymentStatus());
        order.setOrderStatus(orderDetails.getOrderStatus());
        order.setDeliveryStatus(orderDetails.getDeliveryStatus());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
    	orderRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Order not found"));
    	
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
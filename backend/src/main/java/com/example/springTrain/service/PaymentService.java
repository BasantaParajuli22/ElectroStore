package com.example.springTrain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springTrain.model.Order;
import com.example.springTrain.model.Payment;
import com.example.springTrain.repository.OrderRepository;
import com.example.springTrain.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public void deletePayment(Long id) {
    	paymentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Payment not found"));
    	
        paymentRepository.deleteById(id);
    }

    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrder_Id(orderId);
    }
}
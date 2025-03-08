package com.example.springTrain.dto;

import java.util.List;

import com.example.springTrain.enums.DeliveryStatus;
import com.example.springTrain.enums.OrderStatus;
import com.example.springTrain.enums.PaymentStatus;
import com.example.springTrain.model.OrderItem;

public class OrderDto {
    
    private Long customerId;
	private double totalAmount;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private List<OrderItem> orderItems;

    public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
    
    
}
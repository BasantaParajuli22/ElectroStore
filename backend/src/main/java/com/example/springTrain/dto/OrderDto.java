package com.example.springTrain.dto;

import java.util.List;

import com.example.springTrain.enums.DeliveryStatus;
import com.example.springTrain.enums.OrderStatus;
import com.example.springTrain.enums.PaymentStatus;

public class OrderDto {
    
	private Long id;
	private Long customerId;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private List<OrderItemDto> orderItems;
 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	public List<OrderItemDto> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemDto> orderItems) {
		this.orderItems = orderItems;
	}
    
}
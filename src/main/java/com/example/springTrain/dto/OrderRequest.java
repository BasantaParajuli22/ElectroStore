package com.example.springTrain.dto;

import java.util.List;

public class OrderRequest {
	
  private OrderDto orderDto;
  private List<OrderItemDto> orderItemDto;
  
	public OrderDto getOrderDto() {
		return orderDto;
	}
	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}
	public List<OrderItemDto> getOrderItemDto() {
		return orderItemDto;
	}
	public void setOrderItemDto(List<OrderItemDto> orderItemDto) {
		this.orderItemDto = orderItemDto;
	}
	
	
	  
}
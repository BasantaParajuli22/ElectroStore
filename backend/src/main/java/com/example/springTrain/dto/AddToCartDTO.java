package com.example.springTrain.dto;

public class AddToCartDTO {
    private Long productId;
    private int quantity;
    // getters, setters, constructors
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    
}

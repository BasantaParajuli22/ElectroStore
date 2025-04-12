package com.example.springTrain.dto;

public class CartCountDTO {
    private long count;

    public CartCountDTO() {
    }

    public CartCountDTO(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
package com.example.springTrain.exceptions;


public class CreationFailedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CreationFailedException(String message) {
        super(message);
    }
}
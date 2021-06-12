package com.sing3demons.stock_backend.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long id) {
        super("Could not find product : " + id);
    }
}

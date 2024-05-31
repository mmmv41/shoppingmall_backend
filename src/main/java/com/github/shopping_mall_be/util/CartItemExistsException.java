package com.github.shopping_mall_be.util;

public class CartItemExistsException extends RuntimeException {
    public CartItemExistsException(String message) {
        super(message);
    }
}
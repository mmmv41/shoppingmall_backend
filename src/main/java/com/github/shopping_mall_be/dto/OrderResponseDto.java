package com.github.shopping_mall_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {
    private String message;
    private Long cartItemId;
    private Long productId;
    private int quantity;
    private int totalPrice;


}
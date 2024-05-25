package com.github.shopping_mall_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long productId;
    private String productName;
    private String description;
    private Integer price;
    private Integer stock;
    private String imageUrl;
    private String userNickName;

}

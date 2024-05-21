package com.github.shopping_mall_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDto {

    private Integer productId;
    private String productName;
    private String description;
    private Integer price;
    private Integer stock;
//    private String imageUrl;

}

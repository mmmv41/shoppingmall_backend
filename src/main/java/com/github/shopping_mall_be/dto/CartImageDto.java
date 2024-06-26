package com.github.shopping_mall_be.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartImageDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer price;
    private String userNickname;
    private Long cartItemId;
    private Long userId;
    private String productOption;
    private Integer productStatus;
    private Integer Stock;
    private String imageUrl;
    private Integer totalprice;
    private String base64Image;
}

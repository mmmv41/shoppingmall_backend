package com.github.shopping_mall_be.dto;


import com.github.shopping_mall_be.domain.OrderedItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderItemDto {
    private Long orderedItemId;
    private Integer quantity;
    private String description;
    private Integer price;
    private Integer stock;
    private Integer totalPrice;
    private String productName;
    private String imageUrl;
    private Date createAt;
    private Date updateAt;


    public OrderItemDto(OrderedItem orderedItem, Integer stock) {
        this.orderedItemId = orderedItem.getOrderedItemId();
        this.quantity = orderedItem.getQuantity();
        this.description = orderedItem.getDescription();
        this.price = orderedItem.getPrice();
        this.stock = stock; // 매개변수로 받은 최신 재고 정보를 사용
        this.totalPrice = orderedItem.getTotalPrice();
        this.productName = orderedItem.getProduct().getProductName();
        this.imageUrl = orderedItem.getImageUrl();
        this.createAt = orderedItem.getCreatedAt();
        this.updateAt = orderedItem.getUpdatedAt();

    }

//    public OrderItemDto(OrderedItem orderedItem) {
//        this.orderedItemId = orderedItem.getOrderedItemId();
//        this.quantity = orderedItem.getQuantity();
//        this.description = orderedItem.getDescription();
//        this.price = orderedItem.getPrice();
//        this.stock = orderedItem.getStock();
//        this.totalPrice = orderedItem.getTotalPrice();
//        this.productName = orderedItem.getProduct().getProductName();
//    }
}
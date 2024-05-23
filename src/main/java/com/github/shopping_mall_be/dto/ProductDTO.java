package com.github.shopping_mall_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.shopping_mall_be.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    // 상품 등록을 위한 DTO
    private String productName;
    private int price;
    private int stock;
    private String productOption;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    private String description;
    private List<MultipartFile> images = new ArrayList<>();
    private String userName; // 현재 로그인된 사용자의 닉네임

    // Product 엔티티에서 DTO로 변환하는 생성자
    public ProductDTO(Product product) {
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.startDate = product.getStartDate();
        this.endDate = product.getEndDate();
        this.description = product.getDescription();
        this.productOption = product.getProductOption();
    }
}


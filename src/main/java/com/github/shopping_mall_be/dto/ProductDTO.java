package com.github.shopping_mall_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.shopping_mall_be.domain.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    //userId는 토큰 입력하면 자동으로 받아와줌
    private Long productId;


    @NotEmpty(message = "상품 이름은 필수입니다.")
    private String productName;

    @NotNull(message = "가격은 필수입니다.")
    private int price;

    @NotNull(message = "가격은 필수입니다.")
    private int stock;

    @NotNull(message = "가격은 필수입니다.")
    private String productOption;

    @NotNull(message = "가격은 필수입니다.")
    private String userNickName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String description;
    private List<MultipartFile> files = new ArrayList<>();
    private List<String> imagePaths = new ArrayList<>(); // 이미지 경로 추가

    // Product 엔티티에서 DTO로 변환하는 생성자
    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.startDate = product.getStartDate();
        this.endDate = product.getEndDate();
        this.description = product.getDescription();
        this.productOption = product.getProductOption();
        this.userNickName = product.getUser().getUser_nickname();
        this.imagePaths = product.getImagePaths(); // 이미지 경로 설정
    }
}
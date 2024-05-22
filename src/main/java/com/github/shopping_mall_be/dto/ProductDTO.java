package com.github.shopping_mall_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String productName;
    private int price;
    private Date startDate;
    private Date endDate;
    private String description;
    private List<MultipartFile> images;
    private String userName; // 현재 로그인된 사용자의 닉네임

}

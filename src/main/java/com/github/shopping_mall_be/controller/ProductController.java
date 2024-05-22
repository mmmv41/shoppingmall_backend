package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.dto.ProductResponseDto;
import com.github.shopping_mall_be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(    // 페이지 당 5개의 상품 출력 , 페이지는 0부터 시작 . 만약 3번째 페이지 확인하고싶다면 , /api/products?page=2
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String sort) {
        return productService.getAvailableProducts(page, size,sort);
    }

    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable Integer productId) {
        return productService.getProductById(productId);
    }

}
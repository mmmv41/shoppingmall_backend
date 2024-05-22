package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.dto.ProductDTO;
import com.github.shopping_mall_be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 상품 등록
    @PostMapping("/register")
    public ProductDTO registerProduct(@ModelAttribute ProductDTO productDTO, @RequestParam("file") MultipartFile file) throws IOException {
        // 상품 등록 서비스 호출
        ProductDTO registeredProduct = productService.registerProduct(productDTO, file);

//        // 현재 로그인된 사용자의 닉네임 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//
//        // 등록된 상품 정보에 현재 로그인된 사용자의 닉네임 추가
//        registeredProduct.setUserName(userName);

        return registeredProduct;
    }

    // 전체 상품 조회 (판매 종료 날짜가 지나지않은 상품만 조회) 엔드포인트
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllActiveProducts();
    }

    // 상품 수정
    @PutMapping("/{productId}/stock")
    public Product updateProductStock(@PathVariable int productId, @RequestParam int newStock) {
        return productService.updateProductStock(productId, newStock);
    }
}

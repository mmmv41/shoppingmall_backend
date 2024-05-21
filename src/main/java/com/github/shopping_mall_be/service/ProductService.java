package com.github.shopping_mall_be.service;


import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.dto.ProductResponseDto;
import com.github.shopping_mall_be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponseDto> getAvailableProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findAllByStockGreaterThan(0, pageable);

        return productsPage.stream().map(product -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
//            dto.setImageUrl(product.getImageUrl());
            return dto;
        }).collect(Collectors.toList());

    }

    public ProductResponseDto getProductById(Integer productId) { // 추가된 부분
        return productRepository.findById(productId).filter(product -> product.getStock() > 0)
                .map(product -> {
                    ProductResponseDto dto = new ProductResponseDto();
                    dto.setProductId(product.getProductId());
                    dto.setProductName(product.getProductName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setStock(product.getStock());
//                    dto.setImageUrl(product.getImageUrl());
                    return dto;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

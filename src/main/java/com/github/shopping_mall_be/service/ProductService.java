package com.github.shopping_mall_be.service;


import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.dto.ProductDTO;
import com.github.shopping_mall_be.dto.ProductResponseDto;
import com.github.shopping_mall_be.repository.ProductRepository;
import com.github.shopping_mall_be.repository.User.UserJpaRepository;
import com.github.shopping_mall_be.repository.User.UserRepository;
import com.github.shopping_mall_be.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    public List<ProductResponseDto> getAvailableProducts(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productRepository.findAllByStockGreaterThan(0, pageable);

        List<ProductResponseDto> productDtos = productsPage.stream().map(product -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setImageUrl(product.getImageUrl());
            dto.setUserNickName(product.getUser().getUser_nickname());
            return dto;
        }).collect(Collectors.toList());

        if ("asc".equalsIgnoreCase(sort)) {
            productDtos.sort(Comparator.comparing(ProductResponseDto::getPrice));
        } else if ("desc".equalsIgnoreCase(sort)) {
            productDtos.sort(Comparator.comparing(ProductResponseDto::getPrice).reversed());
        }

        return productDtos;
    }


public ProductResponseDto getProductById(Long productId) {
        return productRepository.findById(productId)
                .filter(product -> product.getStock() > 0)
                .map(product -> new ProductResponseDto(
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImageUrl(),
                        product.getUser().getUser_nickname()
                ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ProductDTO registerProduct(String email,ProductDTO productDTO) throws IOException {
        validateProductInfo(productDTO);
        UserEntity user = userRepository.findByEmail2(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 이미지 리스트가 null이면 빈 리스트로 초기화
        List<MultipartFile> images = productDTO.getImages() != null ? productDTO.getImages() : new ArrayList<>();

        List<String> imagePaths = images.stream()
                .map(file -> {
                    try {
                        return fileStorageUtil.storeFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());


        // 상품 정보 설정
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setStartDate(productDTO.getStartDate());
        product.setEndDate(productDTO.getEndDate());
        product.setDescription(productDTO.getDescription());
        product.setProductOption(productDTO.getProductOption());
        product.setUser(user);
        productDTO.setUserNickName(productDTO.getUserNickName());
        // User 설정
        imagePaths.forEach(product::addImage);  // 각 이미지 경로를 Product에 추가

        // 상품 등록
        Product registeredProduct = productRepository.save(product);

        // 등록된 상품 정보를 DTO로 변환하여 반환
        return new ProductDTO(registeredProduct);
    }
    private void validateProductInfo(ProductDTO productDTO) {
        // 필요한 모든 상품 정보가 입력되었는지 확인하는 로직 추가
        // 예: productName, price, startDate, endDate, description 등
        // 필수 정보가 빠진 경우 예외를 던지도록 구현
        if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (productDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (productDTO.getStartDate() == null || productDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (productDTO.getDescription() == null || productDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
    }

}

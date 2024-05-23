package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.dto.ProductDTO;
import com.github.shopping_mall_be.repository.ProductRepository;
import com.github.shopping_mall_be.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageUtil fileStorageUtil;

    public ProductDTO registerProduct(ProductDTO productDTO) throws IOException {
        validateProductInfo(productDTO);

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
        product.setUserName(productDTO.getUserName());
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

    // 판매 상품 조회 메서드 (판매 종료날짜가 지나지 않은 상품만 조회 됨)
    public List<Product> getAllActiveProducts() {
        return productRepository.findByEndDateAfterAndStartDateBeforeOrStartDate(new Date(), new Date(), new Date());
    }


    // 재고 수정 메서드
    public Product updateProduct(int productId, ProductDTO updatedProductDTO) throws IOException {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            // 수정할 부분만 업데이트
            if (updatedProductDTO.getProductName() != null && !updatedProductDTO.getProductName().isEmpty()) {
                product.setProductName(updatedProductDTO.getProductName());
            }
            if (updatedProductDTO.getPrice() > 0) {
                product.setPrice(updatedProductDTO.getPrice());
            }
            if (updatedProductDTO.getStartDate() != null) {
                product.setStartDate(updatedProductDTO.getStartDate());
            }
            if (updatedProductDTO.getEndDate() != null) {
                product.setEndDate(updatedProductDTO.getEndDate());
            }
            if (updatedProductDTO.getDescription() != null && !updatedProductDTO.getDescription().isEmpty()) {
                product.setDescription(updatedProductDTO.getDescription());
            }
            product.setStock(updatedProductDTO.getStock());
            if (updatedProductDTO.getProductOption() != null && !updatedProductDTO.getProductOption().isEmpty()) {
                product.setProductOption(updatedProductDTO.getProductOption());
            }

            // 이미지 업데이트는 추가적인 로직이 필요할 수 있으므로 해당 부분은 적절히 처리

            // 상품 정보 업데이트
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
}





//    private ProductDTO convertToProductDTO(Product product) {
//        // Product 엔티티를 ProductDTO로 변환하여 반환하는 로직
//        // 여기서는 상품 이름, 가격, 판매기간, 설명을 포함하도록 구현
//        // 이미지 경로는 파일 시스템에 저장되어 있으므로 ProductDTO에 포함하지 않음
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setProductName(product.getProductName());
//        productDTO.setPrice(product.getPrice());
//        productDTO.setStartDate(product.getStartDate());
//        productDTO.setEndDate(product.getEndDate());
//        productDTO.setDescription(product.getDescription());
//        return productDTO;
//    }


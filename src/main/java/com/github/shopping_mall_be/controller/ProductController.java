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

//    @Autowired
//    private FileStorageUtil fileStorageUtil;

    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(    // 페이지 당 8개의 상품 출력 , 페이지는 0부터 시작 . 만약 3번째 페이지 확인하고싶다면 , /api/products?page=2
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String sort) {
        return productService.getAvailableProducts(page, size,sort);
    }

    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

//    @PostMapping("/register")
//    public ProductDTO registerProduct(@ModelAttribute ProductDTO productDTO, @RequestParam("files") List<MultipartFile> files) throws IOException {
//        // 다중 파일을 ProductDTO에 설정
//        productDTO.setImages(files);
//
//        // 상품 등록 서비스 호출
//
//        ProductDTO registeredProduct = productService.registerProduct(productDTO);
//        return productService.registerProduct(productDTO);
//    }

//        // 현재 로그인된 사용자의 닉네임 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//
//        // 등록된 상품 정보에 현재 로그인된 사용자의 닉네임 추가
//        registeredProduct.setUserName(userName);


    // 전체 상품 조회 (판매 종료 날짜가 지나지않은 상품만 조회) 엔드포인트
//    @GetMapping
//    public List<Product> getAllProducts() {
//        return productService.getAllActiveProducts();
//    }
//
//    @PutMapping("/{productId}")
//    public Product updateProduct(@PathVariable int productId,
//                                 @RequestBody ProductDTO updatedProductDTO) throws IOException {
//        return productService.updateProduct(productId, updatedProductDTO);
//    }


}
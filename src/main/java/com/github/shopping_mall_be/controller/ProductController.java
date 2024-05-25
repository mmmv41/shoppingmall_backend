package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.dto.DetailProductDto;
import com.github.shopping_mall_be.dto.Jwt.JwtProvider;
import com.github.shopping_mall_be.dto.ProductDTO;
import com.github.shopping_mall_be.dto.ProductResponseDto;
import com.github.shopping_mall_be.repository.User.UserRepository;
import com.github.shopping_mall_be.service.ProductService;
import com.github.shopping_mall_be.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/products")  // http://localhost:8080/api/products?page=2&sort=asc 형태 , sort = asc 오름차순 , desc 내림차순
    public List<ProductResponseDto> getProducts(    // 페이지 당 8개의 상품 출력 , 페이지는 0부터 시작 . 만약 3번째 페이지 확인하고싶다면 , /api/products?page=2
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int size,
                                                    @RequestParam(defaultValue = "") String sort) {
        return productService.getAvailableProducts(page, size, sort);
    }

    @GetMapping("/products/{productId}")
    public DetailProductDto getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/products/user/{userId}")
    public List<ProductResponseDto> getProductsByUserId(@PathVariable Long userId) {
        return productService.getProductsByUserId(userId);
    }

    // 상품 등록
    @PostMapping("/products/register")
    public ProductDTO registerProduct(@ModelAttribute ProductDTO productDTO, @RequestParam("files") List<MultipartFile>
            files, Principal principal) throws IOException {
        String userEmail = principal.getName();

        // 다중 파일을 ProductDTO에 설정
        productDTO.setFiles(files);

        // 상품 등록 서비스 호출
        ProductDTO registeredProduct = productService.registerProduct(userEmail, productDTO);

        // 등록된 상품 정보 반환
        return registeredProduct;
    }


    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password,
                                           @RequestParam("productName") String productName,
                                           @RequestParam("price") int price,
                                           @RequestParam("stock") int stock,
                                           @RequestParam("productOption") String productOption,
                                           @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
                                           @RequestParam("description") String description,
                                           @RequestParam("files") List<MultipartFile> files) {
        try {
            // ProductDTO를 생성하고, 받은 정보로 채웁니다.
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(productName);
            productDTO.setPrice(price);
            productDTO.setStock(stock);
            productDTO.setProductOption(productOption);
            productDTO.setStartDate(startDate);
            productDTO.setEndDate(endDate);
            productDTO.setDescription(description);
            productDTO.setFiles(files); // MultipartFile 리스트를 DTO에 설정

            // ProductService를 통해 상품 정보 업데이트
            ProductDTO updatedProduct = productService.updateProduct(productId, email, productDTO, password);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 저장 중 오류가 발생했습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("상품 정보 업데이트 중 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable Long productId, @RequestParam String email, @RequestParam String password) {
        productService.deleteProduct(productId, email, password);
        return "해당 물건이 성공적으로 삭제 되었습니다.";
    }


}


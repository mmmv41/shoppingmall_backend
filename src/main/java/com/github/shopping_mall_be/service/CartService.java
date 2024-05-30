package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.dto.CartImageDto;
import com.github.shopping_mall_be.dto.CartItemDto;
import com.github.shopping_mall_be.repository.CartItemRepository;
//import com.github.shopping_mall_be.repository.CartRepository;
import com.github.shopping_mall_be.repository.ProductRepository;
import com.github.shopping_mall_be.repository.User.UserRepository;
import com.github.shopping_mall_be.util.CartItemExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Value("${UPLOAD_DIR}")
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;


//     장바구니에 아이템 추가
    public Long addItemToCart(String email, Long productId, Integer quantity) {
        UserEntity user = userRepository.findByEmail2(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getProductStatus() == 1) {
            Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

            if (existingCartItem.isPresent()) {
                throw new CartItemExistsException("해당 물건이 이미 장바구니 내부에 있습니다.");
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCreatedAt(new Date());
                cartItem.setUpdatedAt(new Date());

                cartItem = cartItemRepository.save(cartItem);
                return cartItem.getCartItemId(); // 새로 생성된 CartItem의 ID를 반환
            }
        } else {
            throw new RuntimeException("판매중인 물건이 아닙니다.");
        }
    }


    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }



    // 장바구니 아이템 총 가격 계산
    public Integer calculateTotalPrice(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserUserId(userId);
        int totalPrice = 0;

        for (CartItem item : cartItems) {
            // productStatus가 1인 제품만을 고려
            if (item.getProduct().getProductStatus() == 1) {
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        return totalPrice;
    }

    public void updateCartItem(Long cartItemId, CartItemDto cartItemDto) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartItemId(cartItemId)
                .stream()
                .findFirst();

        // Optional<CartItem>이 비어있지 않은 경우 처리
        CartItem cartItem = cartItemOptional.orElseThrow(() -> new RuntimeException("CartItem not found"));

        // productId와 quantity가 제공되면 업데이트
        if (cartItemDto.getProductId() != null) {
            Product product = productRepository.findById(cartItemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // productStatus가 1인지 확인
            if (product.getProductStatus() != 1) {
                throw new RuntimeException("판매중인 상품만 업데이트 가능합니다. ");
            }

            cartItem.setProduct(product);
        }
        if (cartItemDto.getQuantity() != null) {
            cartItem.setQuantity(cartItemDto.getQuantity());
        }

        cartItem.setUpdatedAt(new Date());
        cartItemRepository.save(cartItem);

    }

    @Transactional
    public void deleteItemFromCartById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public List<CartImageDto> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserUserId(userId).stream().map(cartItem -> {
            CartImageDto dto = new CartImageDto();
            dto.setProductId(cartItem.getProduct().getProductId());
            dto.setProductName(cartItem.getProduct().getProductName());
            dto.setQuantity(cartItem.getQuantity());
            dto.setPrice(cartItem.getProduct().getPrice());
            dto.setUserNickname(cartItem.getUser().getUser_nickname());
            dto.setCartItemId(cartItem.getCartItemId());
            dto.setUserId(cartItem.getUser().getUserId());
            dto.setProductOption(cartItem.getProduct().getProductOption());
            dto.setProductStatus(cartItem.getProduct().getProductStatus());
            dto.setStock(cartItem.getProduct().getStock());
            dto.setImageUrl(cartItem.getProduct().getImageUrl());
            dto.setTotalprice(cartItem.getProduct().getPrice()*cartItem.getQuantity());

            try {
                Path filePath = Paths.get(uploadDir).resolve(cartItem.getProduct().getImageUrl()).normalize();
                byte[] imageBytes = Files.readAllBytes(filePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                dto.setBase64Image(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                // 파일을 읽는 데 실패할 경우, 로그를 남기고 적절한 기본값을 설정

            }

            return dto;
        }).collect(Collectors.toList());
    }

}
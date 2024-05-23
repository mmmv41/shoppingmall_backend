package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.dto.CartItemDto;
import com.github.shopping_mall_be.repository.CartItemRepository;
//import com.github.shopping_mall_be.repository.CartRepository;
import com.github.shopping_mall_be.repository.ProductRepository;
import com.github.shopping_mall_be.repository.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // 장바구니에 아이템 추가
    public void addItemToCart(String email, Long productId, Integer quantity) {
        UserEntity user = userRepository.findByEmail2(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCreatedAt(new Date());
        cartItem.setUpdatedAt(new Date());

        cartItemRepository.save(cartItem);
    }


    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }



    // 장바구니 아이템 총 가격 계산
    public Integer calculateTotalPrice(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserUserId(userId);
        int totalPrice = 0;

        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity(); // 여기도 수정
        }

        return totalPrice;
    }

    public void updateCartItem(Long cartItemId, CartItemDto cartItemDto) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartItemId(cartItemId)
                .stream()
                .findFirst();

        // Optional<CartItem>이 비어있지 않은 경우 처리를 진행합니다.
        CartItem cartItem = cartItemOptional.orElseThrow(() -> new RuntimeException("CartItem not found"));

        // productId와 quantity가 제공되면 업데이트합니다.
        if (cartItemDto.getProductId() != null) {
            Product product = productRepository.findById(cartItemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
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

}
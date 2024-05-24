package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.dto.CartItemDto;
import com.github.shopping_mall_be.dto.CartTotalPriceDto;
import com.github.shopping_mall_be.service.CartService;
import com.github.shopping_mall_be.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/cart")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDto cartItemDto, Principal principal) {
        String userEmail = principal.getName(); // Get the email of the logged-in user
        cartService.addItemToCart(userEmail, cartItemDto.getProductId(), cartItemDto.getQuantity());
        return ResponseEntity.ok("장바구니에 상품이 정상적으로 담겼습니다.");
    }


    @GetMapping("/cart/total-price/{userId}")
    public ResponseEntity<?> getTotalPrice(@PathVariable Long userId) {
        Integer totalPrice = cartService.calculateTotalPrice(userId);
        return ResponseEntity.ok(new CartTotalPriceDto(totalPrice));
    }


    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> getAllCartItems() {
        List<CartItem> cartItems = cartService.getAllCartItems();
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(item -> {
                    CartItemDto dto = new CartItemDto();
                    dto.setUserId(item.getUser().getUserId());
                    dto.setProductId(item.getProduct().getProductId());
                    dto.setQuantity(item.getQuantity());
                    dto.setCartItemId(item.getCartItemId());
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(cartItemDtos);
    }


    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemDto cartItemDto) {
        cartService.updateCartItem(cartItemId, cartItemDto);
        return ResponseEntity.ok("장바구니 물품이 성공적으로 업데이트 되었습니다.");
    }

    @PostMapping("/order/{userId}")
    public ResponseEntity<?> orderItemsFromCart(@PathVariable Long userId) {
        orderService.createOrdersFromUserId(userId);
        return ResponseEntity.ok("상품이 성공적으로 구매되었습니다.");
    }

    @DeleteMapping("/order/{orderedItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Long orderedItemId) {
        orderService.deleteItemFromOrderById(orderedItemId);
        return ResponseEntity.ok("해당 구매 물건이 삭제되었습니다.");
    }


    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<?> deleteItemFromCartById(@PathVariable Long cartItemId) {
        cartService.deleteItemFromCartById(cartItemId);
        return ResponseEntity.ok("장바구니에서 해당 물건이 삭제되었습니다.");
    }
}

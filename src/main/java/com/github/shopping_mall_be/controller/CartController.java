package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.dto.CartItemDto;
import com.github.shopping_mall_be.dto.CartTotalPriceDto;
import com.github.shopping_mall_be.service.CartService;
import com.github.shopping_mall_be.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        cartService.addItemToCart(cartItemDto.getCartId(), cartItemDto.getProductId().intValue(), cartItemDto.getQuantity());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/total-price/{cartId}")
    public ResponseEntity<?> getTotalPrice(@PathVariable Integer cartId) {
        Integer totalPrice = cartService.calculateTotalPrice(cartId);
        return ResponseEntity.ok(new CartTotalPriceDto(totalPrice));
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getAllCartItems() {
        List<CartItem> cartItems = cartService.getAllCartItems();
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(item -> {
                    CartItemDto dto = new CartItemDto();
                    dto.setCartId(item.getCartId());
                    dto.setProductId(item.getProduct().getProductId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(cartItemDtos);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Integer cartId, @RequestBody CartItemDto cartItemDto) {
        cartService.updateCartItem(cartId, cartItemDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/{cartId}")
    public ResponseEntity<?> orderItemsFromCart(@PathVariable Integer cartId) {
        orderService.createOrdersFromCartId(cartId);
        return ResponseEntity.ok().build();
    }


}

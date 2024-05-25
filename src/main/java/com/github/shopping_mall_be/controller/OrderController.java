package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.OrderedItem;
import com.github.shopping_mall_be.dto.OrderItemDto;
import com.github.shopping_mall_be.repository.OrderedItemRepository;
import com.github.shopping_mall_be.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderController {



    private final OrderedItemRepository orderedItemRepository;

    @Autowired
    public OrderController(OrderedItemRepository orderedItemRepository) {
        this.orderedItemRepository = orderedItemRepository;
    }



    @Autowired
    private OrderService orderService;

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


    @GetMapping("/order/{userId}")
    public List<OrderItemDto> getOrderItemsByUserId(@PathVariable Long userId) {
        return orderService.findByUserUserId(userId);
    }




}

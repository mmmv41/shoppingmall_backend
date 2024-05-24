package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.domain.OrderedItem;
import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.domain.UserEntity;
import com.github.shopping_mall_be.dto.OrderItemDto;
import com.github.shopping_mall_be.repository.CartItemRepository;
import com.github.shopping_mall_be.repository.OrderedItemRepository;
import com.github.shopping_mall_be.repository.ProductRepository;
import com.github.shopping_mall_be.repository.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderedItemRepository orderedItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    public void createOrdersFromUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("No CartItems found for userId: " + userId);
        }

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));


            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("구매하려는 상품의 재고가 충분하지 않습니다.");
            }

            OrderedItem orderedItem = new OrderedItem();
            orderedItem.setUser(user);
            orderedItem.setProduct(cartItem.getProduct());
            orderedItem.setQuantity(cartItem.getQuantity());
            orderedItem.setDescription(product.getDescription());
            orderedItem.setPrice(product.getPrice());
            orderedItem.setStock(newStock); // 주문 후 예상 재고를 설정

            int totalPrice = product.getPrice() * cartItem.getQuantity();
            orderedItem.setTotalPrice(totalPrice);

            orderedItemRepository.save(orderedItem);

            // 주문이 성공적으로 저장된 후에만 실제 상품의 재고를 감소시킵니다.
            product.setStock(newStock);
            productRepository.save(product);
        }
    }

    public void deleteItemFromOrderById(Long orderedItemId) {
        OrderedItem orderedItem = orderedItemRepository.findById(orderedItemId)
                .orElseThrow(() -> new RuntimeException("OrderedItem not found"));

        // OrderedItem과 연관된 Product의 재고를 업데이트
        Product product = orderedItem.getProduct();
        int returnedQuantity = orderedItem.getQuantity();
        int currentStock = product.getStock();
        product.setStock(currentStock + returnedQuantity);

        // 상품 재고 업데이트
        productRepository.save(product);

        // 주문 항목 삭제
        orderedItemRepository.delete(orderedItem);
    }

    public List<OrderItemDto> findByUserUserId(Long userId) {
        List<OrderedItem> orderedItems = orderedItemRepository.findByUserUserId(userId);
        return orderedItems.stream()
                .map(OrderItemDto::new) // 이전에 언급한 대로 수정했습니다.
                .collect(Collectors.toList());
    }

}

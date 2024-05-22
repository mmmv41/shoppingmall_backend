package com.github.shopping_mall_be.service;

import com.github.shopping_mall_be.domain.CartItem;
import com.github.shopping_mall_be.domain.OrderedItem;
import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.repository.CartItemRepository;
import com.github.shopping_mall_be.repository.OrderedItemRepository;
import com.github.shopping_mall_be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderedItemRepository orderedItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public void createOrdersFromCartId(Integer cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No CartItems found for cartId: " + cartId);
        }

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderedItem orderedItem = new OrderedItem();
            orderedItem.setProductId(product.getProductId());
            orderedItem.setQuantity(cartItem.getQuantity());
            orderedItem.setDescription(product.getDescription());
            orderedItem.setPrice(product.getPrice());
            orderedItem.setStock(product.getStock());

            int totalPrice = product.getPrice() * cartItem.getQuantity();
            orderedItem.setTotalPrice(totalPrice);

            orderedItemRepository.save(orderedItem);


            int newStock = product.getStock() - cartItem.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("구매하려는 상품의 재고가 충분하지 않습니다.");
            }
            product.setStock(newStock);
            productRepository.save(product);
        }
    }
}

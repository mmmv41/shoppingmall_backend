package com.github.shopping_mall_be.repository;

import com.github.shopping_mall_be.domain.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Integer> {
}

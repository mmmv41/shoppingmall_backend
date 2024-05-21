package com.github.shopping_mall_be.repository;


import com.github.shopping_mall_be.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllByStockGreaterThan(int stock, Pageable pageable);
    Optional<Product> findById(Integer productId);
}

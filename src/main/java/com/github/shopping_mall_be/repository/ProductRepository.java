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
    // 가격 오름차순
//    Page<Product> findAllByStockGreaterThanOrderByPriceAsc(int stock, Pageable pageable);

    // 가격 내림차순
//    Page<Product> findAllByStockGreaterThanOrderByPriceDesc(int stock, Pageable pageable);
}

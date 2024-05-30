package com.github.shopping_mall_be.repository;


import com.github.shopping_mall_be.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStockGreaterThanAndProductStatus(int stock, int productStatus, Pageable pageable);
    Optional<Product> findById(Long productId);
    List<Product> findByUserUserId(Long userId);
    List<Product> findByEndDateAfterAndStartDateBeforeOrStartDate(Date endDate, Date startDate1, Date startDate2);


    Page<Product> findByUserUserIdAndStockGreaterThanAndProductStatus(Long userId, int i, int i1, Pageable pageable);
}

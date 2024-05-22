package com.github.shopping_mall_be.repository;

import com.github.shopping_mall_be.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByEndDateAfterAndStartDateBeforeOrStartDate(Date endDate, Date startDate1, Date startDate2);
}







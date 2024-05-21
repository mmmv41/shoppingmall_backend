package com.github.shopping_mall_be.controller;

import com.github.shopping_mall_be.domain.Product;
import com.github.shopping_mall_be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product registerProduct(@RequestBody Product product) {
        return productService.registerProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

}

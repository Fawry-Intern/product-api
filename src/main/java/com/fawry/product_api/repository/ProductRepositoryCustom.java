package com.fawry.product_api.repository;

import com.fawry.product_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;


public interface ProductRepositoryCustom {
    Page<Product> searchProducts(String name, String description, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}

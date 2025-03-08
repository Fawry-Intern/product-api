package com.fawry.product_api.service;

import com.fawry.product_api.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    void deleteProductById(Long id);
    Product saveProduct(Product product);

}

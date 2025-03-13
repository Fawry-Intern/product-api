package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.entity.Product;
import com.fawry.kafka.events.ProductEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product saveProduct(ProductRequest request);
    void sendProductNotification(ProductEvent event);

}

package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductDTO;
import com.fawry.product_api.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<ProductDTO> getAllProducts();
    Optional<ProductDTO> getProductById(Long id);
    void deleteProductById(Long id);
    ProductDTO saveProduct(Product product);

}

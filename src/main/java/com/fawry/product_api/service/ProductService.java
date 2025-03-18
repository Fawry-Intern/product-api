package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.dto.SearchProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ProductService {
    Page<SearchProductResponse> searchProducts(String name,
                                               String description,
                                               BigDecimal minPrice,
                                               BigDecimal maxPrice,
                                               int page,
                                               int size);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
    void deleteProductById(Long id);
    ProductResponse saveProduct(ProductRequest request);
    ProductResponse updateProduct(Long productId, ProductRequest request);
}


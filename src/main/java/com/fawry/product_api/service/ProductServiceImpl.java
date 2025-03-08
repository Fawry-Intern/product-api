package com.fawry.product_api.service;

import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.product_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new ProductNotFoundException("Product cannot be null.");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ProductNotFoundException("Product name cannot be empty.");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductNotFoundException("Product price must be non-negative.");
        }
    }

}

package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductDTO;
import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.product_api.mapper.ProductMapper;
import com.fawry.product_api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            log.info("Retrieved {} products", products.size());
            return productMapper.toDTOList(products);
        } catch (Exception e) {
            log.error("Error retrieving products: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve products", e);
        }
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO);
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO saveProduct(Product product) {
        validateProduct(product);
        product = productRepository.save(product);
        log.info("Saved product: {}", product);
        return productMapper.toDTO(product);
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

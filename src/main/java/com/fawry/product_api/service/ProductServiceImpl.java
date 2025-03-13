package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.kafka.events.ProductEvent;
import com.fawry.kafka.producers.ProductProducer;
import com.fawry.product_api.mapper.ProductMapper;
import com.fawry.product_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "products")
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTS_CACHE = "products";
    private static final String PRODUCT_LIST_CACHE = "productList";

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;
    private final ProductMapper ProductMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductProducer productProducer, ProductMapper ProductMapper) {
        this.productRepository = productRepository;
        this.productProducer = productProducer;
        this.ProductMapper = ProductMapper;
    }

    @Override
    @Cacheable(cacheNames = PRODUCT_LIST_CACHE, sync = true)
    public List<ProductResponse> getAllProducts() {
        return ProductMapper.mapProductsToResponses(productRepository.findAll());
    }

    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, key = "#id", unless = "#result == null")
    public ProductResponse getProductById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
        return ProductMapper.mapProductToResponse(product);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = PRODUCT_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, key = "#id")
    })
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Caching(
            evict = @CacheEvict(cacheNames = PRODUCT_LIST_CACHE, allEntries = true),
            put = @CachePut(cacheNames = PRODUCTS_CACHE, key = "#result.id")
    )
    @Transactional
    public ProductResponse saveProduct(ProductRequest request) {
        var product = ProductMapper.mapRequestToProduct(request);
        var savedProduct = productRepository.save(product);
        sendProductNotification(ProductMapper.mapProductToEvent(savedProduct));
        return ProductMapper.mapProductToResponse(savedProduct);
    }

    @Override
    public void sendProductNotification(ProductEvent event) {
        productProducer.produceProductEvent(event);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        Product updatedProduct = ProductMapper.mapRequestToProduct(request);
        updatedProduct.setId(productId);

        Product savedProduct = productRepository.save(updatedProduct);
        return ProductMapper.mapProductToResponse(savedProduct);
    }

}

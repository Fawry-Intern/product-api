package com.fawry.product_api.service.impl;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.kafka.events.ProductEvent;
import com.fawry.kafka.producers.ProductProducer;

import com.fawry.product_api.mapper.ProductMapper;
import com.fawry.product_api.repository.ProductRepository;
import com.fawry.product_api.service.ProductService;
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
    private final ProductMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductProducer productProducer, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.productProducer = productProducer;
        this.mapper = mapper;
    }

    @Override
    @Cacheable(cacheNames = PRODUCT_LIST_CACHE,sync = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, key = "#id", unless = "#result == null")
    public Product getProductById(Long id) {
        return productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found"));
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
    public Product saveProduct(ProductRequest request) {
        var product = mapRequestToProduct(request);
        var savedProduct = productRepository.save(product);
        sendProductNotification(mapProductToEvent(product));
        return savedProduct;
    }

    @Override
    public void sendProductNotification(ProductEvent event) {
        productProducer.produceProductEvent(event);
    }

    private ProductEvent mapProductToEvent(Product product) {
        return mapper.mapProductToEvent(product);
    }

    private Product mapRequestToProduct(ProductRequest request) {
        return mapper.mapRequestToProduct(request);
    }
}

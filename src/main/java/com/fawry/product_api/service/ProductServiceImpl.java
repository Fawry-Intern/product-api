package com.fawry.product_api.service;

import com.fawry.kafka.producers.ProductProducer;
import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.kafka.events.ProductEvent;
import com.fawry.product_api.mapper.ProductMapper;
import com.fawry.product_api.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@CacheConfig(cacheNames = "products")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCTS_CACHE = "products";
    private static final String PRODUCT_LIST_CACHE = "productList";

    private final ProductRepository productRepository;
    private final ProductProducer productProducer;
    private final ProductMapper ProductMapper;


    @Override
    @Cacheable(cacheNames = PRODUCT_LIST_CACHE, sync = true)
    public List<ProductResponse> getAllProducts() {
        return ProductMapper.mapProductsToResponses(productRepository.findAll());
    }

    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, key = "#id", unless = "#result == null")
    public ProductResponse getProductById(Long id) {
        var product = findProductById(id);
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
            put = @CachePut(cacheNames = PRODUCTS_CACHE, key = "#result?.id")
    )
    @Transactional
    public ProductResponse saveProduct(ProductRequest request) {
        var product = ProductMapper.mapRequestToProduct(request);
        var savedProduct = productRepository.save(product);
        System.out.println(savedProduct);
        if (savedProduct.getId() == null) {
            throw new IllegalStateException("Product ID is null after saving");
        }
        sendProductNotification(ProductMapper.mapProductToEvent(savedProduct));
        return ProductMapper.mapProductToResponse(savedProduct);
    }

    @Override
    public void sendProductNotification(ProductEvent event) {
        productProducer.produceProductEvent(event);
    }

    @Override
    public ProductResponse updateProduct(Long productId,@RequestBody ProductRequest request) {
        var product = findProductById(productId);

        mergeProductDetails(product, request);

        return ProductMapper.mapProductToResponse(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    private void mergeProductDetails(Product existingProduct, ProductRequest productRequest) {

        if (StringUtils.isNotBlank(productRequest.name())) {
            existingProduct.setName(productRequest.name());
        }

        if (StringUtils.isNotBlank(productRequest.description())) {
            existingProduct.setDescription(productRequest.description());
        }

        if (StringUtils.isNotBlank(productRequest.imageUrl())) {
            existingProduct.setImageUrl(productRequest.imageUrl());
        }

        if (productRequest.price() != null) {
            existingProduct.setPrice(productRequest.price());
        }
    }

}

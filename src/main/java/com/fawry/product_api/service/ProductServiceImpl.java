package com.fawry.product_api.service;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.dto.SearchProductResponse;
import com.fawry.product_api.entity.Product;
import com.fawry.product_api.exception.ProductNotFoundException;
import com.fawry.product_api.mapper.ProductMapper;
import com.fawry.product_api.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Service
@CacheConfig(cacheNames = "products")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final String SEARCH_PRODUCTS_CACHE = "searchProducts";
    private static final String PRODUCTS_CACHE = "products";
    private static final String PRODUCT_LIST_CACHE = "productList";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    @Cacheable(cacheNames = "searchProducts", key = "#name + '-' + #description + '-' + #minPrice + '-' + #maxPrice + '-' + #page + '-' + #size", unless = "#result.isEmpty()")
    public Page<SearchProductResponse> searchProducts(String name,
                                                      String description,
                                                      BigDecimal minPrice,
                                                      BigDecimal maxPrice,
                                                      int page,
                                                      int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.searchProducts(name,description, minPrice, maxPrice, pageable);
        return products.map(productMapper::mapToSearchProductResponse);
    }

    @Override
    @Cacheable(cacheNames = PRODUCT_LIST_CACHE, sync = true)
    public List<ProductResponse> getAllProducts() {
        return productMapper.mapProductsToResponses(productRepository.findAll());
    }

    @Override
    @Cacheable(cacheNames = PRODUCTS_CACHE, key = "#id", unless = "#result == null")
    public ProductResponse getProductById(Long id) {
        var product = findProductById(id);
        return productMapper.mapProductToResponse(product);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SEARCH_PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, key = "#id")
    })
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SEARCH_PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, key = "#result?.id")
    })
    @Transactional
    public ProductResponse saveProduct(ProductRequest request) {
        var product = productMapper.mapRequestToProduct(request);
        var savedProduct = productRepository.save(product);
        System.out.println(savedProduct);
        if (savedProduct.getId() == null) {
            throw new IllegalStateException("Product ID is null after saving");
        }
        return productMapper.mapProductToResponse(savedProduct);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = SEARCH_PRODUCTS_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCT_LIST_CACHE, allEntries = true),
            @CacheEvict(cacheNames = PRODUCTS_CACHE, key = "#productId")
    })
    public ProductResponse updateProduct(Long productId,@RequestBody ProductRequest request) {
        var product = findProductById(productId);

        log.info("After update product{}", product);
        mergeProductDetails(product, request);
        log.info("Before update product{}", product);
        productRepository.save(product);
        return productMapper.mapProductToResponse(product);
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

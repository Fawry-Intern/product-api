package com.fawry.product_api.mapper;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.dto.SearchProductResponse;
import com.fawry.product_api.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductMapper {

    public Product mapRequestToProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .imageUrl(productRequest.imageUrl())
                .build();
    }

    public ProductResponse mapProductToResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .id(product.getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public List<ProductResponse> mapProductsToResponses(List<Product> products) {
        return products.stream()
                .map(this::mapProductToResponse)
                .toList();
    }

    public SearchProductResponse mapToSearchProductResponse(Product product) {
        return SearchProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .build();
    }

}

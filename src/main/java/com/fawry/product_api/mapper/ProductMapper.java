package com.fawry.product_api.mapper;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.dto.ProductResponse;
import com.fawry.product_api.entity.Product;
import com.fawry.kafka.events.ProductEvent;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductMapper {

    public ProductEvent mapProductToEvent(Product product) {
        var event = new ProductEvent();
        event.setName(product.getName());
        event.setDescription(product.getDescription());
        event.setPrice(product.getPrice());
        event.setImageUrl(product.getImageUrl());
        return event;
    }

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

}

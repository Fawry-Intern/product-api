package com.fawry.product_api.mapper;

import com.fawry.product_api.dto.ProductRequest;
import com.fawry.product_api.entity.Product;
import com.fawry.kafka.events.ProductEvent;
import org.springframework.stereotype.Service;

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

    public Product mapRequestToProduct(ProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setDescription(request.description());
        product.setImageUrl(request.imageUrl());
        return product;
    }
}

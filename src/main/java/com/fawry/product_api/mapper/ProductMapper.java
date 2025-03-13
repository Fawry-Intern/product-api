package com.fawry.product_api.mapper;

import com.fawry.product_api.dto.ProductDTO;
import com.fawry.product_api.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if(product == null) return null;

        return ProductDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public Product toEntity(ProductDTO productDTO) {
        if(productDTO == null) return null;

        return Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .build();
    }

    public List<ProductDTO> toDTOList(List<Product> productList) {
        if (productList == null) return null;

        return productList.stream()
                .map(product -> ProductDTO.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .imageUrl(product.getImageUrl())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .build())
                .toList();
    }


}

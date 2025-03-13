package com.fawry.product_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;

    @NotNull(message = "Product Name is mandatory")
    private String name;

    @NotNull(message = "Product Price is mandatory")
    private BigDecimal price;

    @NotNull(message = "Product Description is mandatory")
    private String description;

    @NotNull(message = "Product Image is mandatory")
    private String imageUrl;

    private Instant createdAt;

    private Instant updatedAt;
}

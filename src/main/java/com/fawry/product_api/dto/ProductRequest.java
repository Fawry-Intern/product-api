package com.fawry.product_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        BigDecimal price,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @Pattern(
                regexp = "^(https?|ftp)://[\\w.-]+(?:\\.[\\w\\.-]+)+[/#?]?.*$",
                message = "Invalid image URL format"
        )
        String imageUrl
) implements Serializable {
}

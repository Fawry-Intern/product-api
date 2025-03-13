package com.fawry.product_api.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 100, message = "Product name must between 2 and 100 character")
        String name,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        BigDecimal price,
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        @NotBlank(message = "Image URL is required")
        @Pattern(
                regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
                message = "Invalid image URL format"
        )
        String imageUrl
) implements Serializable {
}

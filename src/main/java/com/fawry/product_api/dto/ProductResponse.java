package com.fawry.product_api.dto;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductResponse (
        Long id,
        String name,
        BigDecimal price,
        String description,
        String imageUrl
) implements Serializable {}

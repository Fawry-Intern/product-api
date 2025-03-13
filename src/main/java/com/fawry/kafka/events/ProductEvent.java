package com.fawry.kafka.events;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
public class ProductEvent implements Serializable {
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    @Override
    public String toString() {
        return "ProductEvent{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

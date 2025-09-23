package com.cositems.api.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class ProductModel {
    @Id
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;

}

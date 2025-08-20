package com.cositems.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "shopping_cart") 
public class ShoppingCart {
    @Id
    private String id;
    private String customerId;
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();
}

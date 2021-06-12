package com.sing3demons.stock_backend.models;

import lombok.*;

@Data
@AllArgsConstructor
public class Product {
    private long id;
    private String name;
    private String image;
    private int price;
    private int stock;
}

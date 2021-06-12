package com.sing3demons.stock_backend.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private MultipartFile image;
    private int price;
    private int stock;
}

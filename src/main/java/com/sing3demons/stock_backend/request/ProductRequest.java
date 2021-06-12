package com.sing3demons.stock_backend.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {
    @NotEmpty(message = "is Empty")
    @Size(min=2,max = 100)
    private String name;
    private MultipartFile image;
    private int price;
    private int stock;
}

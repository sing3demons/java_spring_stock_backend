package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final AtomicLong counter = new AtomicLong();
    private List<Product> productList = new ArrayList<>();

    @GetMapping("")
    public List<Product> getProducts() {
        return productList;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable(name = "id") int id) {
        return productList.stream().filter(p -> p.getId() == id).findFirst().orElseThrow();
    }

    @GetMapping("/search")
    public String getProduct(@RequestParam(name = "name") String name) {
        return "search " + name;
    }

    @PostMapping("")
    public Product createProducts(@RequestBody Product product) {
        Product data = new Product(counter.incrementAndGet(), product.getName(), product.getImage(), product.getPrice(), product.getStock());
        productList.add(data);
        return data;
    }

}

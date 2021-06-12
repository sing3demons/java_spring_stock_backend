package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ProductNotFoundException;
import com.sing3demons.stock_backend.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.nio.file.ProviderNotFoundException;
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
    public Product getProduct(@PathVariable(name = "id") long id) {
        return productList.stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ProductNotFoundException(id));
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

    @PutMapping("/{id}")
    public void update(@RequestBody Product product, @PathVariable("id") long id) {
        Product data;
        productList.stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(r -> {
            r.setName(product.getName());
            r.setImage(product.getImage());
            r.setPrice(product.getPrice());
            r.setStock(product.getStock());
        }, () -> {
            //TODO
        });
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        productList.stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(r -> {
            productList.remove(r);
        }, () -> {
            throw new ProductNotFoundException(id);
        });
    }


}

package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ProductNotFoundException;
import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.request.ProductRequest;
import com.sing3demons.stock_backend.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final AtomicLong counter = new AtomicLong();
    private List<Product> productList = new ArrayList<>();
    private StorageService storageService;

    ProductController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Product> getProducts() {
        return productList;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable(name = "id") long id) {
        return productList.stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ProductNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public String getProduct(@RequestParam(name = "name") String name) {
        return "search " + name;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Product createProducts(ProductRequest productRequest) {
        String fileName = storageService.store(productRequest.getImage());
        System.out.printf("upload: "+fileName);
        Product data = new Product(counter.incrementAndGet(), productRequest.getName(), fileName, productRequest.getPrice(), productRequest.getStock());
        productList.add(data);
        return data;
    }

    @ResponseStatus(HttpStatus.OK)
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        productList.stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(r -> {
            productList.remove(r);
        }, () -> {
            throw new ProductNotFoundException(id);
        });
    }


}

package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ProductNotFoundException;
import com.sing3demons.stock_backend.exception.ValidationException;
import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.repository.ProductRepository;
import com.sing3demons.stock_backend.request.ProductRequest;
import com.sing3demons.stock_backend.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

//@CrossOrigin
@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

    private final AtomicLong counter = new AtomicLong();

    private final List<Product> productList = new ArrayList<>();
    private final StorageService storageService;
    private final ProductRepository productRepository;

    ProductController(StorageService storageService, ProductRepository productRepository) {
        this.storageService = storageService;
        this.productRepository = productRepository;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable(name = "id") long id) {
        return productRepository.findById(id).stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ProductNotFoundException(id));
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public String getProduct(@RequestParam(name = "name") String name) {
        return "search " + name;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProducts(@Valid ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        String fileName = storageService.store(productRequest.getImage());

        Product data = new Product();
        data.setName(productRequest.getName());
        data.setImage(fileName);
        data.setPrice(productRequest.getPrice());
        data.setStock(productRequest.getStock());

        return productRepository.save(data);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Product product, @PathVariable("id") long id) {
        productRepository.findById(id).stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(r -> {
            r.setName(product.getName());
            r.setImage(product.getImage());
            r.setPrice(product.getPrice());
            r.setStock(product.getStock());
            productRepository.save(r);
        }, () -> {
            throw new ProductNotFoundException(id);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        productRepository.findById(id).stream().filter(p -> p.getId() == id).findFirst().ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException(id);
        });
    }


}

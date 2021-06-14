package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ValidationException;
import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.request.ProductRequest;
import com.sing3demons.stock_backend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {


    private final ProductService productService;
    private final Map<String, List<Product>> productRepos = new HashMap<>();
    private final Map<String, Product> productRepo = new HashMap<>();
    private final Map<String, String> msg = new HashMap<>();

    ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getProducts() {
        List<Product> products = productService.findProduct();
        productRepos.put("products", products);
        return new ResponseEntity<>(productRepos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(name = "id") long id) {
//           .stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ProductNotFoundException(id));
        Product product = productService.findProductById(id);

        productRepo.put("product", product);
        return ResponseEntity.ok(productRepo);
    }


    @PostMapping("")
    public ResponseEntity<Object> createProducts(@Valid ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        productService.createProduct(productRequest);

        msg.put("message", "Product is created successfully");
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Valid ProductRequest productRequest, BindingResult bindingResult, @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        productService.updateProduct(productRequest, id);
        msg.put("message", "Product is created successfully");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        msg.put("message", "Product is deleted successfully");
        return new ResponseEntity<>(msg, HttpStatus.NO_CONTENT);
    }


    @GetMapping(path = "/search", params = "name")
    public ResponseEntity<Object> getProduct(@RequestParam(name = "name") String name) {
        Product product = productService.findByProductName(name);
        productRepo.put("product", product);
        return ResponseEntity.ok(productRepo);
    }


    @GetMapping(path = "/search", params = {"name", "stock"})
    public ResponseEntity<Object> searchProductNameAndStock(@RequestParam String name, @RequestParam() int stock) {
        List<Product> products = productService.findByProductNameAndStock(name, stock);
        productRepos.put("products", products);
        return new ResponseEntity<>(productRepos, HttpStatus.OK);
    }


    @GetMapping(path = "/search", params = {"name", "price"})
    public ResponseEntity<Object> searchProductNameAndPrice(@RequestParam String name, @RequestParam() int price) {
        List<Product> products = productService.findByNameAndPrice(name, price);
        productRepos.put("products", products);
        return new ResponseEntity<>(productRepos, HttpStatus.OK);
    }

    @GetMapping("out-of-stock")
    public ResponseEntity<Object> checkOutOfStock() {
        List<Product> products = productService.findByProductOutOfStock();
        productRepos.put("products", products);
        return new ResponseEntity<>(productRepos, HttpStatus.OK);
    }
}

package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ValidationException;
import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.request.ProductRequest;
import com.sing3demons.stock_backend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProducts() {
        return productService.findProduct();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable(name = "id") long id) {
//           .stream().filter(p -> p.getId() == id).findFirst().orElseThrow(() -> new ProductNotFoundException(id));
        return productService.findProductById(id);
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProducts(@Valid ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        return productService.createProduct(productRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product update(@Valid ProductRequest productRequest, BindingResult bindingResult, @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        return productService.updateProduct(productRequest, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        productService.deleteProduct(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search", params = "name")
    public Product getProduct(@RequestParam(name = "name") String name) {
        return productService.findByProductName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search", params = {"name", "stock"})
    public List<Product> searchProductNameAndStock(@RequestParam String name, @RequestParam() int stock) {
        return productService.findByProductNameAndStock(name, stock);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/search", params = {"name", "price"})
    public List<Product> searchProductNameAndPrice(@RequestParam String name, @RequestParam() int price) {
        return productService.findByNameAndPrice(name,price);
    }

    @GetMapping("out-of-stock")
    public List<Product> checkOutOfStock() {
        return productService.findByProductOutOfStock();
    }
}

package com.sing3demons.stock_backend.service;

import com.sing3demons.stock_backend.exception.ProductNotFoundException;
import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.repository.ProductRepository;
import com.sing3demons.stock_backend.request.ProductRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;

@Service
public class ProductServiceImp implements ProductService {
    private final StorageService storageService;
    private final ProductRepository productRepository;

    public ProductServiceImp(StorageService storageService, ProductRepository productRepository) {
        this.storageService = storageService;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findProduct() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        if (products.isEmpty()) {
            return new ArrayList<Product>();
        }
        return products;
    }

    @Override
    public Product findProductById(long id) throws PatternSyntaxException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFoundException(id);

    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        String fileName = storageService.store(productRequest.getImage());

        Product data = new Product();
        data.setName(productRequest.getName());
        data.setImage(fileName);
        data.setPrice(productRequest.getPrice());
        data.setStock(productRequest.getStock());

        return productRepository.save(data);
    }

    @Override
    public Product updateProduct(ProductRequest productRequest, long id) {
        String fileName = storageService.store(productRequest.getImage());
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        if (fileName.isEmpty()) {
            fileName = product.get().getImage();
            System.out.printf("file: " + fileName);
        }

        product.get().setName(productRequest.getName());
        product.get().setImage(fileName);
        product.get().setPrice(productRequest.getPrice());
        product.get().setStock(productRequest.getStock());
        return productRepository.save(product.get());

    }

    @Override
    public void deleteProduct(long id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public Product findByProductName(String name) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findTopByName(name);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFoundException(name);
    }

    @Override
    public List<Product> findByProductNameAndStock(String name, int stock) {
        Optional<List<Product>> product = Optional.ofNullable(productRepository.findByNameContainingAndStockGreaterThanEqualOrderByStockDesc(name, stock));
        return product.orElse(null);
    }

    @Override
    public List<Product> findByProductOutOfStock() {
        return productRepository.checkOutOfStock();
    }

    @Override
    public List<Product> findByNameAndPrice(String name, int price) {
        return productRepository.searchNameAndPrice(name,price);
    }
}

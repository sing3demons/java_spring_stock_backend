package com.sing3demons.stock_backend.service;

import com.sing3demons.stock_backend.models.Product;
import com.sing3demons.stock_backend.request.ProductRequest;

import java.util.List;

public interface ProductService {
    List<Product> findProduct();
    Product findProductById(long id);
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(ProductRequest productRequest,long id);
    void  deleteProduct(long id);

    Product findByProductName(String name);
    List<Product> findByProductNameAndStock(String name,int stock);

    List<Product> findByProductOutOfStock();

    List<Product> findByNameAndPrice(String name,int price);
}

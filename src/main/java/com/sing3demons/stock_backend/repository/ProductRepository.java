package com.sing3demons.stock_backend.repository;

import com.sing3demons.stock_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

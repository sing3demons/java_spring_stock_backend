package com.sing3demons.stock_backend.repository;

import com.sing3demons.stock_backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // SELECT * FROM Product WHERE name = '' LIMIT 1  (top=limit)
    Optional<Product> findTopByName(String name);

    // SELECT * FROM Product WHERE name LIKE '%name%' AND stock >= x order by stock
    List<Product> findByNameContainingAndStockGreaterThanEqualOrderByStockDesc(String name,int stock);

    @Query(value = "SELECT * FROM PRODUCT WHERE STOCK = 0",nativeQuery = true)
    List<Product> checkOutOfStock();

    @Query(value = "SELECT * FROM Product WHERE name LIKE %:product_name% AND price > :price",nativeQuery = true)
    List<Product> searchNameAndPrice(@Param("product_name") String name, int price);
}

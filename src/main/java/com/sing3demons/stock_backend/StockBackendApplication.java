package com.sing3demons.stock_backend;

import com.sing3demons.stock_backend.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StockBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageservice){
        return args-> storageservice.init();
    }

}

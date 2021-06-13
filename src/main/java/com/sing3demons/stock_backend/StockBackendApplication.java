package com.sing3demons.stock_backend;

import com.sing3demons.stock_backend.service.StorageService;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class StockBackendApplication {

    public static void main(String[] args) {
        Map<String, Object> env = Dotenv.load()
                .entries().stream()
                .collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue));

        new SpringApplicationBuilder(StockBackendApplication.class)
                .environment(new StandardEnvironment() {
                    @Override
                    protected void customizePropertySources(@NotNull MutablePropertySources propertySources) {
                        super.customizePropertySources(propertySources);
                        propertySources.addLast(new MapPropertySource("dotenvProperties", env));
                    }
                }).run(args);
//        SpringApplication.run(StockBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageservice) {
        return args -> storageservice.init();
    }

}

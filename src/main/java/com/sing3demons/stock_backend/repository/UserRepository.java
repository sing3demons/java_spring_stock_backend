package com.sing3demons.stock_backend.repository;

import com.sing3demons.stock_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}

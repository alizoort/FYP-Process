package com.example.workflow.repositories;

import com.example.workflow.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findUserByUsername(String u);
}

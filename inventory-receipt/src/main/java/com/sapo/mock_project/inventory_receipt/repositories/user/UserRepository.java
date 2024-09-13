package com.sapo.mock_project.inventory_receipt.repositories.user;

import com.sapo.mock_project.inventory_receipt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByPhone(String username);

    boolean existsByPhone(String phone);
}

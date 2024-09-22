package com.sapo.mock_project.inventory_receipt.repositories.user;

import com.sapo.mock_project.inventory_receipt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByPhone(String username);

    boolean existsByPhone(String phone);
}

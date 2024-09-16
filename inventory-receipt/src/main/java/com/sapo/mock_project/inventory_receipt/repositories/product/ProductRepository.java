package com.sapo.mock_project.inventory_receipt.repositories.product;

import com.sapo.mock_project.inventory_receipt.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}

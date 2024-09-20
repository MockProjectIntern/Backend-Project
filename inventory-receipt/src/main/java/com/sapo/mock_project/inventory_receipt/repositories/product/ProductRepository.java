package com.sapo.mock_project.inventory_receipt.repositories.product;

import com.sapo.mock_project.inventory_receipt.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
}

package com.sapo.mock_project.inventory_receipt.repositories.combine;

import com.sapo.mock_project.inventory_receipt.entities.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
}

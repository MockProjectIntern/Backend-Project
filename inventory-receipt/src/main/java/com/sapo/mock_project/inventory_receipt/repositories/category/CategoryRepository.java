package com.sapo.mock_project.inventory_receipt.repositories.category;

import com.sapo.mock_project.inventory_receipt.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}

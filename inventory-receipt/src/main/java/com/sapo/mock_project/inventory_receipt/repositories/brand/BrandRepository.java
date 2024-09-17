package com.sapo.mock_project.inventory_receipt.repositories.brand;

import com.sapo.mock_project.inventory_receipt.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandRepository extends JpaRepository<Brand, String>, JpaSpecificationExecutor<Brand> {
    boolean existsByName(String name);
}

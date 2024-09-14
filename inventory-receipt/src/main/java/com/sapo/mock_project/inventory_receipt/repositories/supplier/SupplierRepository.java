package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierRepository extends JpaRepository<Supplier, String>, JpaSpecificationExecutor<Supplier> {
    boolean existsByName(String name);

}

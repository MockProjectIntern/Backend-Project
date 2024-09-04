package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierCategory extends JpaRepository<Supplier, Long> {
}

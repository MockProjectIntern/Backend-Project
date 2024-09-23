package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.entities.DebtSupplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtSupplierRepository extends JpaRepository<DebtSupplier, String> {
}

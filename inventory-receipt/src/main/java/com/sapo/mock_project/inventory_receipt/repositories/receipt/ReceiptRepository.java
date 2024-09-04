package com.sapo.mock_project.inventory_receipt.repositories.receipt;

import com.sapo.mock_project.inventory_receipt.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}

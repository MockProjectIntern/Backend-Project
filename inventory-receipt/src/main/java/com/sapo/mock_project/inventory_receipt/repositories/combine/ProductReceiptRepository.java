package com.sapo.mock_project.inventory_receipt.repositories.combine;

import com.sapo.mock_project.inventory_receipt.entities.ProductReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReceiptRepository extends JpaRepository<ProductReceipt, Long> {
}

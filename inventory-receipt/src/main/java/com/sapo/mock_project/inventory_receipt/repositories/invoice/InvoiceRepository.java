package com.sapo.mock_project.inventory_receipt.repositories.invoice;

import com.sapo.mock_project.inventory_receipt.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}

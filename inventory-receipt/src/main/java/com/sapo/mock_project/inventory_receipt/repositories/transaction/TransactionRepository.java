package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
}

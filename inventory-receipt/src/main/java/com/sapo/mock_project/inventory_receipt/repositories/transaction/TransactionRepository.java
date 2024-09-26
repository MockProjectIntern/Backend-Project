package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<Transaction> findByIdAndTenantId(String id, String tenantId);
}

package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, String>, JpaSpecificationExecutor<TransactionCategory> {
    boolean existsByNameAndType(String name, TransactionType type);

    boolean existsBySubId(String subId);

    Optional<TransactionCategory> findBySubId(String subId);
}

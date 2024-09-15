package com.sapo.mock_project.inventory_receipt.repositories.transaction;

import com.sapo.mock_project.inventory_receipt.constants.enums.TransactionType;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, String>, JpaSpecificationExecutor<TransactionCategory> {
    boolean existsByNameAndType(String name, TransactionType type);
}

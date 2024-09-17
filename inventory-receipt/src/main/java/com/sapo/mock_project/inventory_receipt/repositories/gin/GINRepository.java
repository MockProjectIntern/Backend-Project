package com.sapo.mock_project.inventory_receipt.repositories.gin;

import com.sapo.mock_project.inventory_receipt.entities.GIN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GINRepository extends JpaRepository<GIN,String>, JpaSpecificationExecutor<GIN> {
}

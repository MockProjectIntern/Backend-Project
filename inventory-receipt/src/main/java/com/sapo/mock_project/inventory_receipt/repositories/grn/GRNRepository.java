package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.entities.GRN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GRNRepository extends JpaRepository<GRN, String>, JpaSpecificationExecutor<GRN> {

}

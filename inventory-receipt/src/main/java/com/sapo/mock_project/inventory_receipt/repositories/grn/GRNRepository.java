package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.entities.GRN;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GRNRepository extends JpaRepository<GRN, String> {
}

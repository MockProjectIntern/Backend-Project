package com.sapo.mock_project.inventory_receipt.repositories.refundinfor;

import com.sapo.mock_project.inventory_receipt.entities.RefundInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundInformationRepository extends JpaRepository<RefundInformation, String> {
}

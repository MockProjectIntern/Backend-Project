package com.sapo.mock_project.inventory_receipt.repositories.price_adjustment;

import com.sapo.mock_project.inventory_receipt.entities.PriceAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PriceAdjustmentRepository extends JpaRepository<PriceAdjustment, String>, JpaSpecificationExecutor<PriceAdjustment> {

}
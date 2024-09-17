package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GRNProductRepoiotry extends JpaRepository<GRNProduct,String> {
}

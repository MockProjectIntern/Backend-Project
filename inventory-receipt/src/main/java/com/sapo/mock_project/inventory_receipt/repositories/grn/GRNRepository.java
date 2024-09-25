package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GRNRepository extends JpaRepository<GRN, String>, JpaSpecificationExecutor<GRN> {
    boolean existsBySubId(String subId);

    Page<GRN> findBySupplier(Supplier supplier, Pageable pageable);
}

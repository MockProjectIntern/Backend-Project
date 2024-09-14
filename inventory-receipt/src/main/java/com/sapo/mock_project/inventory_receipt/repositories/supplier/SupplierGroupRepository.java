package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierGroupRepository extends JpaRepository<SupplierGroup, String> {
    boolean existsByName(String name);

    boolean existsById(String id);

    Page<SupplierGroup> findAllByStatus(SupplierGroupStatus status, Pageable pageable);
}

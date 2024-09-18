package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierGroupStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.suppliergroup.GetListSupplierGroupRequest;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierGroupRepository extends JpaRepository<SupplierGroup, String>, JpaSpecificationExecutor<SupplierGroup> {
    boolean existsByName(String name);

    boolean existsBySubId(String subId);
}

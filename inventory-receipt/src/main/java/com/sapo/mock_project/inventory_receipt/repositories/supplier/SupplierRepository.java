package com.sapo.mock_project.inventory_receipt.repositories.supplier;

import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, String>, JpaSpecificationExecutor<Supplier> {
    boolean existsByName(String name);

    boolean existsBySubId(String subId);

    @Query("SELECT s.id, s.name, s.phone FROM Supplier s WHERE s.name LIKE %:name%")
    Page<Object[]> findAllByName(String name, Pageable pageable);

    @Query(value = """
        SELECT s.id, s.name, s.phone, s.address, s.current_debt, s.total_refund,
            COALESCE(COUNT(g.id), 0) AS grn_count,  COALESCE(SUM(g.total_value), 0) AS grn_total_value
        FROM suppliers s
        LEFT JOIN grns g ON g.supplier_id = s.id
        WHERE s.id = :supplierId
        GROUP BY s.id
        """, nativeQuery = true)
    List<Object[]> getDetailMoney(String supplierId);
}

package com.sapo.mock_project.inventory_receipt.repositories.product;

import com.sapo.mock_project.inventory_receipt.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("""
            SELECT COALESCE(SUM(od.quantity - COALESCE(od.importedQuantity, 0)), 0)
            FROM Product p
            LEFT JOIN OrderDetail od ON p.id = od.product.id
            LEFT JOIN Order o ON od.order.id = o.id
            WHERE p.id = :productId
            AND (o.status = 'PENDING' OR o.status = 'PARTIAL')
            """)
    List<Object[]> getIncomingQuantity(String productId);
}

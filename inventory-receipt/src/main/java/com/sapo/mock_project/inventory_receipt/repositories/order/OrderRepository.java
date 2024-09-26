package com.sapo.mock_project.inventory_receipt.repositories.order;

import com.sapo.mock_project.inventory_receipt.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    boolean existsBySubIdAndTenantId(String subId, String tenantId);

    Optional<Order> findByIdAndTenantId(String id, String tenantId);
}

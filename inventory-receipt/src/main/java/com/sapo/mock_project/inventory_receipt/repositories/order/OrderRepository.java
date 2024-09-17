package com.sapo.mock_project.inventory_receipt.repositories.order;

import com.sapo.mock_project.inventory_receipt.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}

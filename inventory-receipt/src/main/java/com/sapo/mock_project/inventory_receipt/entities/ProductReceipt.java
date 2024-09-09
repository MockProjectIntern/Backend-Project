package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_receipts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReceipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal money;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}

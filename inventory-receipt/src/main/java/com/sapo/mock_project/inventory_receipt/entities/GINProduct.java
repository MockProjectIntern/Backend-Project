package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "gin_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GINProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal actualStock;

    private BigDecimal discrepancyQuantity;

    private String reason;

    private String note;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "gin_id")
    private GIN gin;
}

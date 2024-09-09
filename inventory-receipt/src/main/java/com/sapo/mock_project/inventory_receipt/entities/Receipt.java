package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "receipts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Receipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal total;

    @OneToMany(mappedBy = "receipt")
    private List<ProductReceipt> productReceipts;

    @OneToOne(mappedBy = "receipt")
    private Invoice invoice;
}

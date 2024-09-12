package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "supplier_groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierGroup {
    @Id
    private Long id;

    private String name;

    private String note;
}

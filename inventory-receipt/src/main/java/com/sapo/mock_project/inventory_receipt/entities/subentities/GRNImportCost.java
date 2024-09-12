package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRNImportCost {
    private String name;

    private BigDecimal value;
}

package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GRNImportCost {
    private String name;

    private BigDecimal value;

    @Override
    public String toString() {
        return "GRNImportCost{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}

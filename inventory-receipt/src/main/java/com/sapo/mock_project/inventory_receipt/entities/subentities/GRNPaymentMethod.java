package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRNPaymentMethod {
    private String method;

    private BigDecimal amount;

    private LocalDateTime date;

    private String reference;
}

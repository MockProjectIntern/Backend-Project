package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRefundTransactionRequest {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    private TransactionMethod paymentMethod;
}

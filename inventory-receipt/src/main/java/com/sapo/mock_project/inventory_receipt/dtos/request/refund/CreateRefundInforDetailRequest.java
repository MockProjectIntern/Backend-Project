package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRefundInforDetailRequest {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("refunded_price")
    private BigDecimal refundedPrice;

    @JsonProperty("amount")
    private BigDecimal amount;
}

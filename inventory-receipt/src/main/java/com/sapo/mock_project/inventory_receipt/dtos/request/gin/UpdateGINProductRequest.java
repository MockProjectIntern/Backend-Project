package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateGINProductRequest {
    @JsonProperty("actual_stock")
    private BigDecimal actualStock;

    @JsonProperty("discrepancy_quantity")
    private BigDecimal discrepancyQuantity;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("note")
    private String note;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("product_id")
    private String productId;
}

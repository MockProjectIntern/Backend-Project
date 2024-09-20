package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateGRNProductRequest {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("tax")
    private BigDecimal tax;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("note")
    private String note;
}

package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDetailRequest {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("tax")
    private BigDecimal tax;

    @JsonProperty("unit")
    private String unit;
}

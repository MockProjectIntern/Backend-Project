package com.sapo.mock_project.inventory_receipt.dtos.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuickCreateProductRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("cost_price")
    private BigDecimal costPrice;

    @JsonProperty("retail_price")
    private BigDecimal retailPrice;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("unit")
    private String unit;
}

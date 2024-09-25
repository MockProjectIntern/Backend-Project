package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateOrderRequest {
    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("expected_at")
    private LocalDate expectedAt;

    @JsonProperty("products")
    private List<CreateOrderDetailRequest> products;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

    @JsonProperty("discount")
    private BigDecimal discount;
}

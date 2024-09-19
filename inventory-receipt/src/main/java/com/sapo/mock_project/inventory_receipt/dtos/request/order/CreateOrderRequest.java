package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateOrderRequest {
    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("expected_at")
    private LocalDate expectedAt;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("products")
    private List<CreateOrderDetailRequest> products;
}

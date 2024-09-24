package com.sapo.mock_project.inventory_receipt.dtos.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class OrderResponse extends BaseResponse {
    @JsonProperty("supplier")
    private Map<String, Object> supplierDetail;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("expected_at")
    @JsonFormat(pattern = DateTimePattern.YYYYMMDDHHMMSS, shape = JsonFormat.Shape.STRING)
    private LocalDateTime expectedAt;

    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetails;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("tax")
    private BigDecimal tax;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;
}

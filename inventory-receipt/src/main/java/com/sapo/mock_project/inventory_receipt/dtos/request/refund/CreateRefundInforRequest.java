package com.sapo.mock_project.inventory_receipt.dtos.request.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateRefundInforRequest {
    @JsonProperty("grn_id")
    private String grnId;

    @JsonProperty("total_cost")
    private BigDecimal totalCost;

    @JsonProperty("total_tax")
    private BigDecimal totalTax;

    @JsonProperty("total_discount")
    private BigDecimal totalDiscount;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("refund_detail")
    private List<CreateRefundInforDetailRequest> refundDetail;

    @JsonProperty("transaction")
    private CreateRefundTransactionRequest transaction;
}

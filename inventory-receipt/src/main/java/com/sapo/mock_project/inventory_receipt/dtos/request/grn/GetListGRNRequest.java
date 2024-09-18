package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetListGRNRequest {
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("status")
    private String status;

    @JsonProperty("received_status")
    private String receivedStatus;

    @JsonProperty("payment_status")
    private String paymentStatus;

    @JsonProperty("return_status")
    private String returnStatus;

    @JsonProperty("refund_status")
    private String refundStatus;

    @JsonProperty("supplier_id")
    private String supplierId;

    @JsonProperty("created_date")
    private LocalDate createdDate;

    @JsonProperty("expected_delivery_at")
    private LocalDate expectedDeliveryAt;

    @JsonProperty("received_at")
    private LocalDate receivedAt;

    @JsonProperty("cancelled_at")
    private LocalDate cancelledAt;

    @JsonProperty("payment_at")
    private LocalDate paymentAt;

    @JsonProperty("return_at")
    private LocalDate endedAt;

    @JsonProperty("user_created_id")
    private String userCreatedId;

    @JsonProperty("user_imported_id")
    private String userImportedId;

    @JsonProperty("user_cancelled_id")
    private String userCancelledId;

    @JsonProperty("user_ended_id")
    private String userEndedId;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;
}

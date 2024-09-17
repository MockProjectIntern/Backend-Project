package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetListGRNRequest {
    private String keyword;
    private String status;
    private String receivedStatus;
    private String paymentStatus;
    private String returnStatus;

    private String refundStatus;

    private String supplierId;

    private LocalDate createdDate;
    private LocalDate expectedDeliveryAt;
    private LocalDate receivedAt;
    private LocalDate cancelledAt;
    private LocalDate paymentAt;
    private LocalDate endedAt;

    private String userCreatedId;
    private String userImportedId;
    private String userCancelledId;
    private String userEndedId;

    private String tags;
    private String note;
}

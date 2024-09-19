package com.sapo.mock_project.inventory_receipt.dtos.request.order;

import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetListOrderRequest {
    private List<OrderStatus> statuses;

    private List<String> supplierIds;

    private LocalDate startCreatedAt;

    private LocalDate endCreatedAt;

    private LocalDate startExpectedAt;

    private LocalDate endExpectedAt;

    private List<String> productIds;

    private List<String> userCreatedIds;

    private List<String> userCompletedIds;

    private List<String> userCancelledIds;
}

package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateGRNRequest {
    private String supplierId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("expected_delivery_at")
    private LocalDate expectedDeliveryAt;

    @JsonProperty("status")
    private GRNStatus status;

    @JsonProperty("received_status")
    private GRNReceiveStatus receivedStatus;

    @JsonProperty("products")
    private List<GRNProduct> products;

    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("discount")
    private BigDecimal discount;

    @JsonProperty("import_costs")
    private List<GRNImportCost> importCosts;

    @JsonProperty("payment_methods")
    private List<GRNPaymentMethod> paymentMethods;
}

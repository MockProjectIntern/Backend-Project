package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.dtos.request.refund.CreateRefundInforRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.refundinfor.RefundInformationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.REFUND_INFORMATION)
@RequiredArgsConstructor
public class RefundInformationController {
    private final RefundInformationService refundInformationService;

    @PostMapping(value = "/create.json")
    public ResponseEntity<ResponseObject<Object>> createRefundInformation(@Valid @RequestBody CreateRefundInforRequest request) {
        return refundInformationService.createRefundInformation(request);
    }
}

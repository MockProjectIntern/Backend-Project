package com.sapo.mock_project.inventory_receipt.services.price_adjustment;

import com.sapo.mock_project.inventory_receipt.dtos.request.price_adjustment.CreatePriceAdjustmentRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PriceAdjustmentService {
    ResponseEntity<ResponseObject<Object>> createPriceAdjustment(CreatePriceAdjustmentRequest request);

}
package com.sapo.mock_project.inventory_receipt.services.grn;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface GRNService {
    ResponseEntity<ResponseObject<Object>> createGRN(CreateGRNRequest request);
}

package com.sapo.mock_project.inventory_receipt.services.order;

import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.GetListOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OrderService {
    ResponseEntity<ResponseObject<Object>> createOrder(CreateOrderRequest request);

    ResponseEntity<ResponseObject<Object>> filterOrder(GetListOrderRequest request, Map<String, Boolean> filterParams, int page, int size);
}

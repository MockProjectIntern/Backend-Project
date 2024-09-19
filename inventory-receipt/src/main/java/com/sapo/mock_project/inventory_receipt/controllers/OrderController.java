package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.GetListOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.order.OrderService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.ORDER)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create.json")
    public ResponseEntity<ResponseObject<Object>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterOrder(@Valid @RequestBody GetListOrderRequest request,
                                                              @RequestParam(value = "page", defaultValue = "1") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.ORDER, httpServletRequest);

        return orderService.filterOrder(request, filterParams, page, size);
    }
}

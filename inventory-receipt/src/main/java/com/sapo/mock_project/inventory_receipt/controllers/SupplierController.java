package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.SUPPLIER)
@RequiredArgsConstructor
public class SupplierController {
}

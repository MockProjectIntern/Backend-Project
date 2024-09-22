package com.sapo.mock_project.inventory_receipt.controllers;

import com.sapo.mock_project.inventory_receipt.constants.BaseEndpoint;
import com.sapo.mock_project.inventory_receipt.constants.NameFilterFromCookie;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.CreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.GetListProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.QuickCreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.UpdateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.services.product.ProductService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "${api.prefix}" + BaseEndpoint.PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/quick-create.json")
    public ResponseEntity<ResponseObject<Object>> quickCreateProduct(@Valid @RequestBody QuickCreateProductRequest request) {
        return productService.quickCreateProduct(request);
    }

    @PostMapping("/create.json")
    public ResponseEntity<ResponseObject<Object>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/update.json/{id}")
    public ResponseEntity<ResponseObject<Object>> updateProduct(@PathVariable String id,
                                                                @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @PostMapping("/filter.json")
    public ResponseEntity<ResponseObject<Object>> filterProduct(@RequestBody GetListProductRequest request,
                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                                HttpServletRequest httpServletRequest) {
        Map<String, Boolean> filterParams = CommonUtils.getFilterParamsFromCookie(NameFilterFromCookie.PRODUCT, httpServletRequest);

        return productService.filterProduct(request, filterParams, page, size);
    }
}

package com.sapo.mock_project.inventory_receipt.services.product;

import com.sapo.mock_project.inventory_receipt.dtos.request.product.CreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.GetListProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.QuickCreateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.UpdateProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {
    ResponseEntity<ResponseObject<Object>> quickCreateProduct(QuickCreateProductRequest request);

    ResponseEntity<ResponseObject<Object>> createProduct(CreateProductRequest request);

    ResponseEntity<ResponseObject<Object>> updateProduct(String productId, UpdateProductRequest request);

    ResponseEntity<ResponseObject<Object>> filterProduct(GetListProductRequest request, Map<String, Boolean> filterParams, int page, int size);
}

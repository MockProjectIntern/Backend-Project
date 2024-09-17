package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.price_adjustment.CreatePriceAdjustmentRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.CreateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.SupplierDetail;
import com.sapo.mock_project.inventory_receipt.entities.PriceAdjustment;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface PriceAdjustmentMapper {
    PriceAdjustment mapToEntity(CreatePriceAdjustmentRequest request);

    SupplierDetail mapToResponse(Supplier supplier);
}

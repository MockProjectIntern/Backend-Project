package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.GRNProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNProductDetail;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GRNProductMapper {
    GRNProduct mapToEntity(GRNProductRequest request);

    GRNProductDetail mapToResponse(GRNProduct grnProduct);

//    void updateFromDTO(UpdateGRNRequest request, GRN existingGRN);
}

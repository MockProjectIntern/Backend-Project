package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNProductRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.UpdateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNDetail;
import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GRNMapper {
    GRN mapToEntity(CreateGRNRequest request);

    GRNDetail mapToResponse(GRN grn);

    void updateFromDTO(UpdateGRNRequest request,@MappingTarget GRN existingGRN);
}

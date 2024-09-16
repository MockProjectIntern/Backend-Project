package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.entities.GRN;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GRNMapper {
    GRN mapToEntity(CreateGRNRequest request);
}

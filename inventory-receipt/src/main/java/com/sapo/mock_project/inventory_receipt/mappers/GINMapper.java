package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINDetail;
import com.sapo.mock_project.inventory_receipt.entities.GIN;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GINMapper extends {
    GIN mapToEntity(CreateGINRequest request);
    GINDetail mapToResponse(GIN gin);

    void updateFromDTO(UpdateGINRequest request,@MappingTarget GIN existingGIN);
}

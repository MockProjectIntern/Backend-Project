package com.sapo.mock_project.inventory_receipt.mappers;

import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderDetailRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.entities.Order;
import com.sapo.mock_project.inventory_receipt.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    Order mapToEntity(CreateOrderRequest request);

    OrderDetail mapToEntityDetail(CreateOrderDetailRequest request);
}

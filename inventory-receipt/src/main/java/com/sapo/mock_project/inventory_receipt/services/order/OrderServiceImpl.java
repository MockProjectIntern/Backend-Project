package com.sapo.mock_project.inventory_receipt.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderDetailRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.CreateOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.order.GetListOrderRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderDetailResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.OrderMapper;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderDetailRepository;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderRepository;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderRepositoryCustom;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.supplier.SupplierService;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final SupplierRepository supplierRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final ProductRepository productRepository;

    private final SupplierService supplierService;

    private final OrderMapper orderMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> createOrder(CreateOrderRequest request) {
        try {
            if (request.getSubId() != null && orderRepository.existsBySubId(request.getSubId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.ORDER_SUB_ID_EXISTED));
            }
            Supplier exstingSupplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            User userCreated = authHelper.getUser();

            Order newOrder = orderMapper.mapToEntity(request);

            List<OrderDetail> newOrderDetail = new ArrayList<>();
            for (CreateOrderDetailRequest detailRequest : request.getProducts()) {
                Product exstingProduct = productRepository.findById(detailRequest.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                OrderDetail orderDetail = orderMapper.mapToEntityDetail(detailRequest);

                orderDetail.setOrder(newOrder);
                orderDetail.setProduct(exstingProduct);

                newOrderDetail.add(orderDetail);
            }

            newOrder.setSupplier(exstingSupplier);
            newOrder.setUserCreated(userCreated);
            newOrder.setOrderDetails(newOrderDetail);
            newOrder.setStatus(OrderStatus.PENDING);

            newOrder.initializeOrder();
            newOrder.calculateTotalPrice();

            orderRepository.save(newOrder);
            orderDetailRepository.saveAll(newOrderDetail);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> filterOrder(GetListOrderRequest request,
                                                              Map<String, Boolean> filterParams,
                                                              int page, int size) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String filterJson = objectMapper.writeValueAsString(filterParams);

            List<OrderGetListResponse> orderGetListResponses = orderRepositoryCustom.getFilteredOrders(
                    filterJson,
                    request.getKeyword(),
                    CommonUtils.joinParams(request.getStatuses()),
                    CommonUtils.joinParams(request.getSupplierIds()),
                    request.getStartCreatedAt(),
                    request.getEndCreatedAt(),
                    request.getStartExpectedAt(),
                    request.getEndExpectedAt(),
                    CommonUtils.joinParams(request.getProductIds()),
                    CommonUtils.joinParams(request.getUserCreatedIds()),
                    CommonUtils.joinParams(request.getUserCompletedIds()),
                    CommonUtils.joinParams(request.getUserCancelledIds()),
                    page,
                    size
            );

            int totalOrders = orderRepositoryCustom.countTotalOrders(
                    filterJson,
                    request.getKeyword(),
                    CommonUtils.joinParams(request.getStatuses()),
                    CommonUtils.joinParams(request.getSupplierIds()),
                    request.getStartCreatedAt(),
                    request.getEndCreatedAt(),
                    request.getStartExpectedAt(),
                    request.getEndExpectedAt(),
                    CommonUtils.joinParams(request.getProductIds()),
                    CommonUtils.joinParams(request.getUserCreatedIds()),
                    CommonUtils.joinParams(request.getUserCompletedIds()),
                    CommonUtils.joinParams(request.getUserCancelledIds())
            );

            int totalPages = (int) Math.ceil((double) totalOrders / size);

            Pagination pagination = Pagination.<Object>builder()
                    .data(orderGetListResponses)
                    .totalPage(totalPages)
                    .totalItems(totalOrders)
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getOrderById(String id) {
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isEmpty()) {
                return ResponseUtil.error404Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.ORDER_NOT_FOUND));
            }
            Order existingOrder = order.get();

            Map<String, Object> supplierDetail = ((Map<String, Object>) supplierService.getDetailMoney(existingOrder.getSupplier().getId()).getBody().getData());

            OrderResponse orderResponse = orderMapper.mapToResponse(existingOrder);
            orderResponse.setSupplierDetail(supplierDetail);
            orderResponse.setUserCreatedName(existingOrder.getUserCreated().getFullName());
            for (OrderDetailResponse orderDetail : orderResponse.getOrderDetails()) {
                Optional<Map<String, Object>> optionalDataMap = existingOrder.getOrderDetails().stream()
                        .filter(detail -> detail.getSubId().equals(orderDetail.getSubId()))
                        .map(detail -> Map.of(
                                "productName", detail.getProduct().getName(),
                                "productId", detail.getProduct().getId(),
                                "productImage", detail.getProduct().getImages() == null ? null : detail.getProduct().getImages().get(0)
                        ))
                        .findFirst();

                Map<String, Object> dataMap = optionalDataMap.get();

                orderDetail.setProductName((String) dataMap.get("productName"));
                orderDetail.setProductId((String)dataMap.get("productId"));
                orderDetail.setProductImage((ProductImage) dataMap.get("productImage"));
            }

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.ORDER_GET_DETAIL_SUCCESSFULLY), orderResponse);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

}

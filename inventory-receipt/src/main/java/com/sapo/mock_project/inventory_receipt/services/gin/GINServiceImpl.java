package com.sapo.mock_project.inventory_receipt.services.gin;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.CreateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.GetListGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.gin.UpdateGINRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.gin.GINDetail;

import com.sapo.mock_project.inventory_receipt.entities.*;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.GINMapper;
import com.sapo.mock_project.inventory_receipt.repositories.gin.GINProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.gin.GINRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.user.UserRepository;

import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các dịch vụ liên quan đến phiếu xuất kho (GIN).
 * Cung cấp các phương thức để tạo mới, lấy chi tiết, lọc, cập nhật và xóa phiếu xuất kho.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GINServiceImpl implements GINService {

    private final GINRepository ginRepository;
    private final GINProductRepository ginProductRepository;
    private final GINMapper ginMapper;
    private final AuthHelper authHelper;
    private final LocalizationUtils localizationUtils;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Tạo mới một phiếu xuất kho.
     *
     * @param request Đối tượng chứa thông tin tạo mới phiếu xuất kho.
     * @return Phản hồi với kết quả của thao tác tạo mới.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createGIN(CreateGINRequest request) {
        try {
            // Kiểm tra xem ID có tồn tại không
            if (request.getId() != null && ginRepository.existsById(request.getId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.GIN_ID_EXISTED));
            }


                    // Tạo đối tượng GIN mới và lưu vào cơ sở dữ liệu
                    GIN newGIN = ginMapper.mapToEntity(request);
            List<GINProduct> ginProducts = new ArrayList<>();
            for (GINProduct ginProduct : request.getProducts()) {
                Product product = productRepository.findById(ginProduct.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                ginProduct.setDiscrepancyQuantity(ginProduct.getActualStock().subtract(ginProduct.getProduct().getQuantity()));
                ginProduct.setProduct(product);
                ginProduct.setGin(newGIN);

                ginProducts.add(ginProduct);
            }
            newGIN.setProducts(ginProducts);

            newGIN.setUserInspection(userRepository.findById(request.getUserInspectionId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND))));
            User userCreated = authHelper.getUser();
            newGIN.setUserCreated(userCreated);
            newGIN.setUserBalanced(userCreated);
            newGIN.setStatus(GINStatus.CHECKING);

            ginRepository.save(newGIN);
            ginProductRepository.saveAll(newGIN.getProducts());

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết của phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần lấy thông tin.
     * @return Phản hồi với thông tin chi tiết của phiếu xuất kho.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getGINById(String id) {
        try {
            GIN gin = ginRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            GINDetail response = ginMapper.mapToResponse(gin);

            response.setUserCreatedName(gin.getUserCreated().getFullName());
            response.setUserBalancedName(gin.getUserBalanced().getFullName());
            response.setUserInspectionName(gin.getUserInspection().getFullName());

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lọc danh sách phiếu xuất kho theo các tiêu chí.
     *
     * @param request Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Các trường cần ánh xạ và lọc.
     * @param page Trang hiện tại.
     * @param size Số lượng bản ghi trên mỗi trang.
     * @return Phản hồi với danh sách phiếu xuất kho đã lọc.
     */
//    @Override
//    public ResponseEntity<ResponseObject<Object>> filterGIN(GetListGINRequest request, Map<String, Boolean> filterParams, int page, int size) {
//        try {
//            GINSpecification ginSpecification = new GINSpecification(request);
//            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "createdAt"));
//
//            Page<GIN> ginPage = ginRepository.findAll(ginSpecification, pageable);
//
//            // Ánh xạ các đối tượng GIN thành GINGetListResponse
//            List<GINGetListResponse> responseList = ginPage.getContent().stream().map(gin -> {
//                GINGetListResponse response = new GINGetListResponse();
//
//                // Duyệt qua các entry trong filterParams
//                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
//                    String field = entry.getKey();
//                    Boolean includeField = entry.getValue();
//
//                    // Ánh xạ các trường dựa trên filterParams
//                    if (includeField) {
//                        try {
//                            // Lấy phương thức set tương ứng
//                            Method setMethod = response.getClass().getMethod("set" + StringUtils.snakeCaseToCamelCase(field), String.class);
//
//                            // Gọi phương thức set với giá trị tương ứng từ đối tượng gin
//                            setMethod.invoke(response, CommonUtils.getFieldValue(gin, field));
//                        } catch (Exception e) {
//                            // Xử lý lỗi nếu không thể lấy phương thức hoặc gọi phương thức
//                            throw new RuntimeException("Failed to map field " + field + " using reflection", e);
//                        }
//                    }
//                }
//
//                return response;
//            }).toList();
//
//            Pagination<Object> responsePageable = Pagination.<Object>builder()
//                    .data(responseList)
//                    .totalItems(ginPage.getTotalElements())
//                    .totalPage(ginPage.getTotalPages())
//                    .build();
//
//            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_GET_ALL_SUCCESSFULLY), responsePageable);
//        } catch (Exception e) {
//            return ResponseUtil.error500Response(e.getMessage());
//        }
//    }

    /**
     * Cập nhật thông tin của phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật.
     * @return Phản hồi với kết quả của thao tác cập nhật.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateGIN(String id, UpdateGINRequest request) {
        try {
            GIN existingGIN = ginRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

            // Cập nhật thông tin của phiếu xuất kho
            ginMapper.updateFromDTO(request, existingGIN);

            List<GINProduct> updatedProducts = new ArrayList<>();
            for (GINProduct requestGinProduct : request.getProducts()) {
                Product product = productRepository.findById(requestGinProduct.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                if (requestGinProduct.getId() != null) {
                    // Cập nhật sản phẩm hiện tại
                    GINProduct existingGINProduct = ginProductRepository.findById(requestGinProduct.getId())
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_PRODUCT_NOT_FOUND)));

                    // Cập nhật các trường dữ liệu của sản phẩm
                    existingGINProduct.setNote(requestGinProduct.getNote());
                    existingGINProduct.setActualStock(requestGinProduct.getActualStock());
                    existingGINProduct.setDiscrepancyQuantity(requestGinProduct.getActualStock().subtract(requestGinProduct.getProduct().getQuantity()));
                    existingGINProduct.setReason(requestGinProduct.getReason());
                    existingGINProduct.setProduct(product);
                    existingGINProduct.setGin(existingGIN);

                    updatedProducts.add(existingGINProduct);
                } else {
                    // Thêm sản phẩm mới
                    log.info("UpProduct: {}", requestGinProduct.toString());
                    requestGinProduct.setProduct(product);
                    requestGinProduct.setGin(existingGIN);
                    updatedProducts.add(requestGinProduct);
                }
            }

            // Xóa các sản phẩm không còn tồn tại trong request
            List<String> requestProductIds = request.getProducts().stream()
                    .filter(p -> p.getId() != null)
                    .map(GINProduct::getId)
                    .toList();

            requestProductIds.stream().map(idProduct -> {
                if (updatedProducts.stream().noneMatch(p -> p.getId().equals(idProduct))) {
                    ginProductRepository.deleteById(idProduct);
                }
                return idProduct;
            }).toList();

            existingGIN.setProducts(updatedProducts);

            ginRepository.save(existingGIN);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

/**
 * Xóa một phiếu xuất kho theo ID.
 *
 * @param id ID của phiếu xuất kho cần xóa.
 * @return Phản hồi với kết quả của thao tác
 * xóa.
 */
@Override
public ResponseEntity<ResponseObject<Object>> deleteGIN(String id) {
    try {
        GIN existingGIN = ginRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GIN_NOT_FOUND)));

        // Đánh dấu phiếu xuất kho là đã xóa
        existingGIN.setStatus(GINStatus.DELETED);
        ginRepository.save(existingGIN);

        return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_DELETE_SUCCESSFULLY));
    } catch (Exception e) {
        return ResponseUtil.error500Response(e.getMessage());
    }
}

    /**
     * Cân đối phiếu xuất kho theo ID.
     *
     * @param id ID của phiếu xuất kho cần cân đối.
     * @return Phản hồi với kết quả của thao tác cân đối.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> balanceGIN(String id) {
        try {


            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GIN_BALANCE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}

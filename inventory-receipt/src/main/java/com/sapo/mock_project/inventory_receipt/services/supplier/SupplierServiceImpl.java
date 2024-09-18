package com.sapo.mock_project.inventory_receipt.services.supplier;

import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.CreateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.UpdateSupplierRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.SupplierDetail;
import com.sapo.mock_project.inventory_receipt.dtos.response.supplier.SupplierGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import com.sapo.mock_project.inventory_receipt.entities.SupplierGroup;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.SupplierMapper;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierGroupRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.SupplierSpecification;
import com.sapo.mock_project.inventory_receipt.utils.CommonUtils;
import com.sapo.mock_project.inventory_receipt.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các dịch vụ liên quan đến nhà cung cấp (Supplier).
 * Cung cấp các phương thức để tạo mới, lấy chi tiết, lọc, cập nhật và xóa nhà cung cấp.
 */
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierGroupRepository supplierGroupRepository;
    private final SupplierMapper supplierMapper;
    private final LocalizationUtils localizationUtils;

    /**
     * Tạo mới một nhà cung cấp.
     *
     * @param request Đối tượng chứa thông tin tạo mới nhà cung cấp.
     * @return Phản hồi với kết quả của thao tác tạo mới.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createSupplier(CreateSupplierRequest request) {
        try {
            // Kiểm tra xem ID có tồn tại không
            if (request.getSubId() != null && supplierRepository.existsBySubId(request.getSubId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_ID_EXISTED));
            }
            // Kiểm tra xem tên có tồn tại không
            if (supplierRepository.existsByName(request.getName())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_NAME_EXISTED));
            }
            // Lấy nhóm nhà cung cấp
            SupplierGroup existingSupplierGroup = supplierGroupRepository.findById(request.getSupplierGroupId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND)));

            // Tạo đối tượng Supplier mới và lưu vào cơ sở dữ liệu
            Supplier newSupplier = supplierMapper.mapToEntity(request);
            newSupplier.setGroup(existingSupplierGroup);
            newSupplier.setStatus(SupplierStatus.ACTIVE);
            newSupplier.setCurrentDebt(BigDecimal.ZERO);

            supplierRepository.save(newSupplier);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết của nhà cung cấp theo ID.
     *
     * @param supplierId ID của nhà cung cấp cần lấy thông tin.
     * @return Phản hồi với thông tin chi tiết của nhà cung cấp.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getSupplierById(String supplierId) {
        try {
            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            SupplierDetail response = supplierMapper.mapToResponse(supplier);
            response.setSupplierGroupName(supplier.getGroup().getName());

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lọc danh sách nhà cung cấp theo các tiêu chí.
     *
     * @param request Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Các trường cần ánh xạ và lọc.
     * @param page Trang hiện tại.
     * @param size Số lượng bản ghi trên mỗi trang.
     * @return Phản hồi với danh sách nhà cung cấp đã lọc.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> filterSupplier(GetListSupplierRequest request, Map<String, Boolean> filterParams, int page, int size) {
        try {
            SupplierSpecification supplierspecification = new SupplierSpecification(request);
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "name"));

            Page<Supplier> supplierPage = supplierRepository.findAll(supplierspecification, pageable);

            // Ánh xạ các đối tượng Supplier thành SupplierGetListResponse
            List<SupplierGetListResponse> responseList = supplierPage.getContent().stream().map(supplier -> {
                SupplierGetListResponse response = new SupplierGetListResponse();

                // Duyệt qua các entry trong filterParams
                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    // Ánh xạ các trường dựa trên filterParams
                    if (includeField) {
                        try {
                            // Lấy phương thức set tương ứng
                            Method setMethod = response.getClass().getMethod("set" + StringUtils.snakeCaseToCamelCase(field), String.class);

                            // Gọi phương thức set với giá trị tương ứng từ đối tượng supplier
                            setMethod.invoke(response, CommonUtils.getFieldValue(supplier, field));
                        } catch (Exception e) {
                            // Xử lý lỗi nếu không thể lấy phương thức hoặc gọi phương thức
                            throw new RuntimeException("Failed to map field " + field + " using reflection", e);
                        }
                    }
                }

                return response;
            }).toList();

            Pagination<Object> responsePageable = Pagination.<Object>builder()
                    .data(responseList)
                    .totalItems(supplierPage.getTotalElements())
                    .totalPage(supplierPage.getTotalPages())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_GET_ALL_SUCCESSFULLY), responsePageable);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin của nhà cung cấp theo ID.
     *
     * @param supplierId ID của nhà cung cấp cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật.
     * @return Phản hồi với kết quả của thao tác cập nhật.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateSupplier(String supplierId, UpdateSupplierRequest request) {
        try {
            Supplier existingSupplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            // Kiểm tra và xử lý các điều kiện cập nhật
            if (request.getName() != null && !request.getName().equals(existingSupplier.getName()) && supplierRepository.existsByName(request.getName())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_NAME_EXISTED));
            }
            if (request.getSubId() != null && !request.getSubId().equals(existingSupplier.getId()) && supplierRepository.existsBySubId(request.getSubId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_ID_EXISTED));
            }
            if (request.getSupplierGroupId() != null && !request.getSupplierGroupId().equals(existingSupplier.getGroup().getId())) {
                SupplierGroup existingSupplierGroup = supplierGroupRepository.findById(request.getSupplierGroupId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND)));
                existingSupplier.setGroup(existingSupplierGroup);
            }

            supplierMapper.updateFromDTO(request, existingSupplier);

            supplierRepository.save(existingSupplier);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Xóa một nhà cung cấp theo ID.
     *
     * @param supplierId ID của nhà cung cấp cần xóa.
     * @return Phản hồi với kết quả của thao tác xóa.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> deleteSupplier(String supplierId) {
        try {
            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            // Đánh dấu nhà cung cấp là đã xóa
            supplier.setStatus(SupplierStatus.DELETED);
            supplierRepository.save(supplier);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_DELETE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}

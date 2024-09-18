package com.sapo.mock_project.inventory_receipt.services.grn;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNPaymentStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNReceiveStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.GetListGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.UpdateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNDetail;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNProductDetail;
import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import com.sapo.mock_project.inventory_receipt.entities.Product;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNHistory;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.GRNMapper;
import com.sapo.mock_project.inventory_receipt.mappers.GRNProductMapper;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNRepository;
import com.sapo.mock_project.inventory_receipt.repositories.order.OrderRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.GRNSpecification;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Triển khai các dịch vụ liên quan đến phiếu nhập hàng (GRN).
 * Cung cấp các phương thức để tạo mới, lấy chi tiết, cập nhật và xóa GRN.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GRNServiceImpl implements GRNService {

    private final GRNRepository grnRepository;
    private final GRNProductRepository grnProductRepository;
    private final ProductRepository productRepository;
    private final GRNMapper grnMapper;
    private final GRNProductMapper grnProductMapper;
    private final SupplierRepository supplierRepository;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;
    private final OrderRepository orderRepository;

    /**
     * Tạo mới một GRN.
     *
     * @param request Đối tượng chứa thông tin tạo mới GRN.
     * @return Phản hồi với kết quả của thao tác tạo mới.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createGRN(CreateGRNRequest request) {
        try {
            // Kiểm tra xem GRN đã tồn tại chưa
            if (request.getSubId() != null && grnRepository.existsBySubId(request.getSubId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.GRN_EXISTED));
            }

            // Ánh xạ thông tin từ request sang entity và lưu vào cơ sở dữ liệu
            GRN newGRN = grnMapper.mapToEntity(request);
            User user = authHelper.getUser();
            newGRN.setUserCreated(user);
            newGRN.setStatus(GRNStatus.TRADING);
            newGRN.setReceivedStatus(GRNReceiveStatus.NOT_ENTERED);

            // Kiểm tra xem nhà cung cấp có tồn tại không
            if (request.getSupplierId() != null) {
                newGRN.setSupplier(supplierRepository.findById(request.getSupplierId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND))));
            }


            // Thêm các sản phẩm vào GRN
            List<GRNProduct> grnProducts = new ArrayList<>();
            for (GRNProduct grnProduct : request.getProducts()) {
                Product product = productRepository.findById(grnProduct.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                grnProduct.setProduct(product);
                grnProduct.setGrn(newGRN);

                grnProducts.add(grnProduct);
            }
            newGRN.setGrnProducts(grnProducts);

            // Nếu nhập hàng từ order thì set order cho GRN
            if (request.getOrderId() != null) {
                newGRN.setOrder(orderRepository.findById(request.getOrderId())
                        .orElseThrow(() -> new DataNotFoundException("Order Not found")));
            }

            // Tính toán giá trị của GRN
            newGRN.calculatorValue();

            // Thêm lịch sử tạo mới GRN
            newGRN.setHistories(new ArrayList<>());
            GRNHistory grnHistory = GRNHistory.builder().date(LocalDateTime.now())
                    .userExecuted(user.getFullName())
                    .function("Nhập hàng")
                    .operation("Tạo đơn nhập hàng").build();
            newGRN.getHistories().add(grnHistory);


            grnRepository.save(newGRN);
            grnProductRepository.saveAll(newGRN.getGrnProducts());

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }


    /**
     * Lấy thông tin chi tiết của GRN theo ID.
     *
     * @param id ID của GRN cần lấy thông tin.
     * @return Phản hồi với thông tin chi tiết của GRN.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getGRNById(String id) {
        try {
            // Lấy thông tin GRN từ cơ sở dữ liệu
            GRN grn = grnRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND)));

            // Ánh xạ thông tin GRN sang DTO phản hồi
            GRNDetail response = grnMapper.mapToResponse(grn);
            response.setSupplierName(grn.getSupplier().getName());
            response.setSupplierPhone(grn.getSupplier().getPhone());
            response.setSupplierAddress(grn.getSupplier().getAddress());
            response.setUserCreatedName(grn.getUserCreated().getFullName());

            // Ánh xạ thông tin các sản phẩm trong GRN sang DTO phản hồi
            List<GRNProductDetail> grnProductDetails = grn.getGrnProducts().stream().map(grnProduct -> grnProductMapper.mapToResponse(grnProduct)).toList();
            response.setProducts(grnProductDetails);
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_GET_DETAIL_SUCCESSFULLY), response);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin của GRN theo ID.
     *
     * @param id      ID của GRN cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật.
     * @return Phản hồi với kết quả của thao tác cập nhật.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateGRN(String id, UpdateGRNRequest request) {
        try {
            // Lấy thông tin GRN từ cơ sở dữ liệu
            GRN existingGRN = grnRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND)));

            // Cập nhật thông tin GRN từ request
            grnMapper.updateFromDTO(request, existingGRN);

            User user = authHelper.getUser();


            // Xử lý các sản phẩm trong GRN
            List<GRNProduct> updatedProducts = new ArrayList<>();
            for (GRNProduct requestGrnProduct : request.getProducts()) {
                Product product = productRepository.findById(requestGrnProduct.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                if (requestGrnProduct.getId() != null) {
                    // Cập nhật sản phẩm hiện tại
                    GRNProduct existingGRNProduct = grnProductRepository.findById(requestGrnProduct.getId())
                            .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_PRODUCT_NOT_FOUND)));

                    // Cập nhật các trường dữ liệu của sản phẩm
                    existingGRNProduct.setQuantity(requestGrnProduct.getQuantity());
                    existingGRNProduct.setPrice(requestGrnProduct.getPrice());
                    existingGRNProduct.setDiscount(requestGrnProduct.getDiscount());
                    existingGRNProduct.setTax(requestGrnProduct.getTax());
                    existingGRNProduct.setProduct(product);
                    existingGRNProduct.setGrn(existingGRN);

                    updatedProducts.add(existingGRNProduct);
                } else {
                    // Thêm sản phẩm mới
                    log.info("UpProduct: {}", requestGrnProduct.toString());
                    requestGrnProduct.setProduct(product);
                    requestGrnProduct.setGrn(existingGRN);
                    updatedProducts.add(requestGrnProduct);
                }
            }

            // Xóa các sản phẩm không còn tồn tại trong request
            List<String> requestProductIds = request.getProducts().stream()
                    .filter(p -> p.getId() != null)
                    .map(GRNProduct::getId)
                    .toList();

            requestProductIds.stream().map(idProduct -> {
                if (updatedProducts.stream().noneMatch(p -> p.getId().equals(idProduct))) {
                    grnProductRepository.deleteById(idProduct);
                }
                return idProduct;
            }).toList();

            existingGRN.setGrnProducts(updatedProducts);

            existingGRN.calculatorValue();

            GRNHistory grnHistory = GRNHistory.builder().date(LocalDateTime.now())
                    .userExecuted(user.getFullName())
                    .function(existingGRN.getReceivedStatus() == GRNReceiveStatus.ENTERED ? "Nhập hàng" : "Cập nhật")
                    .operation("Cập nhật thông tin đơn nhập").build();

            if (request.getPaymentMethods() != null) {
                if (existingGRN.getPaymentStatus() == GRNPaymentStatus.PAID) {
                    grnHistory.setFunction("Thanh toán");
                    existingGRN.setUserEnded(user);
                    existingGRN.setEndedAt(LocalDate.now());
                } else if (existingGRN.getPaymentStatus() == GRNPaymentStatus.PARTIAL_PAID) {
                    grnHistory.setFunction("Thanh toán một phần");
                }
            }

            // Thêm lịch sử mới vào GRN
            existingGRN.getHistories().add(grnHistory);

            grnProductRepository.saveAll(existingGRN.getGrnProducts());
            grnRepository.save(existingGRN);


            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }


    /**
     * Xóa một GRN theo ID.
     *
     * @param id ID của GRN cần xóa.
     * @return Phản hồi với kết quả của thao tác xóa.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> deleteGRN(String id) {
        try {
            GRN grn = grnRepository.findById(id).orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND)));

            grn.setStatus(GRNStatus.CANCELLED);
            User user = authHelper.getUser();
            grn.setUserCancelled(user);
            grn.setUserEnded(user);
            grn.setCancelledAt(LocalDate.now());
            grn.setEndedAt(LocalDate.now());

            grnRepository.save(grn);


            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_DELETE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy danh sách GRN dựa trên các tiêu chí lọc.
     *
     * @param request      Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Các trường cần lọc.
     * @param page         Trang cần lấy.
     * @param size         Số lượng phần tử trên mỗi trang.
     * @return Phản hồi với danh sách GRN theo các tiêu chí lọc.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> filterGRN(GetListGRNRequest request, Map<String, Boolean> filterParams, int page, int size) {
        try {
            GRNSpecification grnSpecification = new GRNSpecification(request);
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "createdAt"));

            Page<GRN> grnPage = grnRepository.findAll(grnSpecification, pageable);

            // Ánh xạ các đối tượng GRN thành GRNDetailResponse
            List<GRNDetail> responseList = grnPage.getContent().stream().map(grn -> {
                GRNDetail response = new GRNDetail();

                // Duyệt qua các entry trong filterParams
                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    if (includeField) {
                        try {
                            // Lấy phương thức set tương ứng
                            Method setMethod = response.getClass().getMethod("set" + StringUtils.snakeCaseToCamelCase(field), String.class);

                            // Gọi phương thức set với giá trị tương ứng từ đối tượng grn
                            setMethod.invoke(response, CommonUtils.getFieldValue(grn, field));
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to map field " + field + " using reflection", e);
                        }
                    }
                }

                return response;
            }).toList();

            Pagination<Object> responsePageable = Pagination.<Object>builder()
                    .data(responseList)
                    .totalItems(grnPage.getTotalElements())
                    .totalPage(grnPage.getTotalPages())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_GET_ALL_SUCCESSFULLY), responsePageable);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Nhập hàng cho GRN.
     *
     * @param id ID của GRN cần nhập hàng.
     * @return Phản hồi với kết quả của thao tác nhập hàng.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> importGRN(String id) {
        try {
            // Lấy thông tin GRN từ cơ sở dữ liệu
            GRN grn = grnRepository.findById(id).orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.GRN_NOT_FOUND)));

            if (grn.getReceivedStatus() == GRNReceiveStatus.ENTERED) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.GRN_IMPORTED));
            }
            grn.setReceivedStatus(GRNReceiveStatus.ENTERED);
            grn.setUserImported(authHelper.getUser());
            grn.setReceivedAt(LocalDate.now());

            // Thêm lịch sử nhập hàng cho GRN
            grn.getHistories().add(GRNHistory.builder().date(LocalDateTime.now())
                    .userExecuted(authHelper.getUser().getFullName())
                    .function("Nhập hàng")
                    .operation("Nhập hàng").build());

            // Nhập hàng cho các sản phẩm trong GRN, Triển khai sau
            updateProductQuantity(grn);

            grnRepository.save(grn);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_IMPORT_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    private void updateProductQuantity(GRN existingGRN) {

    }
}

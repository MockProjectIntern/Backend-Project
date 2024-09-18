package com.sapo.mock_project.inventory_receipt.services.price_adjustment;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.PriceAdjustmentStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.price_adjustment.CreatePriceAdjustmentRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.entities.PriceAdjustment;
import com.sapo.mock_project.inventory_receipt.entities.Product;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.PriceAdjustmentMapper;
import com.sapo.mock_project.inventory_receipt.repositories.price_adjustment.PriceAdjustmentRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAdjustmentServiceImpl implements PriceAdjustmentService {
    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final LocalizationUtils localizationUtils;
    private final ProductRepository productRepository;
    private final PriceAdjustmentMapper priceAdjustmentMapper;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> createPriceAdjustment(CreatePriceAdjustmentRequest request) {
        try {
            // Kiểm tra xem ID có tồn tại không
            if (request.getSubId() != null && priceAdjustmentRepository.existsBySubId(request.getSubId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.SUPPLIER_ID_EXISTED));
            }
            // Lấy nhóm nhà cung cấp
            Product existingProduct = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_GROUP_NOT_FOUND)));

            User userCreated = authHelper.getUser();

            // Tạo đối tượng Supplier mới và lưu vào cơ sở dữ liệu
            PriceAdjustment newPriceAdjustment = priceAdjustmentMapper.mapToEntity(request);
            newPriceAdjustment.setStatus(PriceAdjustmentStatus.APPROVED);
            newPriceAdjustment.setProduct(existingProduct);
            newPriceAdjustment.setUserCreated(userCreated);

            priceAdjustmentRepository.save(newPriceAdjustment);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.SUPPLIER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}

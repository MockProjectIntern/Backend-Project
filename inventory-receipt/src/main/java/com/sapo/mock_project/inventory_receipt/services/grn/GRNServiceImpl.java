package com.sapo.mock_project.inventory_receipt.services.grn;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.GRNPaymentStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.grn.CreateGRNRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.entities.GRN;
import com.sapo.mock_project.inventory_receipt.entities.GRNProduct;
import com.sapo.mock_project.inventory_receipt.entities.Product;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.mappers.GRNMapper;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.grn.GRNRepository;
import com.sapo.mock_project.inventory_receipt.repositories.product.ProductRepository;
import com.sapo.mock_project.inventory_receipt.repositories.supplier.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GRNServiceImpl implements GRNService {
    private final GRNRepository grnRepository;
    private final GRNProductRepository grnProductRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    private final GRNMapper grnMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    @Override
    public ResponseEntity<ResponseObject<Object>> createGRN(CreateGRNRequest request) {
        try {
            if (request.getId() != null && grnRepository.existsById(request.getId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.GRN_ID_EXISTED));
            }
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.SUPPLIER_NOT_FOUND)));

            GRN newGRN = grnMapper.mapToEntity(request);
            newGRN.setSupplier(supplier);
            newGRN.setUserCreated(authHelper.getUser());
            newGRN.setPaymentStatus(GRNPaymentStatus.PAID);

            List<GRNProduct> grnProducts = new ArrayList<>();
            for (GRNProduct grnProduct : request.getProducts()) {
                Product product = productRepository.findById(grnProduct.getProductId())
                        .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.PRODUCT_NOT_FOUND)));

                grnProduct.setProduct(product);
                grnProduct.setGrn(newGRN);

                grnProducts.add(grnProduct);
            }
            newGRN.setGrnProducts(grnProducts);

            newGRN.calculatorValue();

            grnRepository.save(newGRN);
            grnProductRepository.saveAll(newGRN.getGrnProducts());

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.GRN_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}

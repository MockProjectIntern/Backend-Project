package com.sapo.mock_project.inventory_receipt.services.transaction;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionStatus;
import com.sapo.mock_project.inventory_receipt.dtos.internal.transaction.AutoCreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.CreateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.GetListTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.request.transaction.UpdateTransactionRequest;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.transaction.TransactionGetListResponse;
import com.sapo.mock_project.inventory_receipt.entities.Transaction;
import com.sapo.mock_project.inventory_receipt.entities.TransactionCategory;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.exceptions.NoActionForOperationException;
import com.sapo.mock_project.inventory_receipt.mappers.TransactionMapper;
import com.sapo.mock_project.inventory_receipt.repositories.transaction.TransactionCategoryRepository;
import com.sapo.mock_project.inventory_receipt.repositories.transaction.TransactionRepository;
import com.sapo.mock_project.inventory_receipt.services.specification.TransactionSpecification;
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
import java.util.List;
import java.util.Map;

/**
 * Dịch vụ xử lý các thao tác liên quan đến  (transaction).
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;

    private final TransactionMapper transactionMapper;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    /**
     * Tạo mới một phiếu thu/chi dựa trên yêu cầu đầu vào.
     * Nếu mã phiếu thu/chi đã tồn tại, trả về thông báo lỗi.
     *
     * @param request Đối tượng chứa thông tin tạo phiếu thu/chi.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createTransaction(CreateTransactionRequest request) {
        try {
            if (request.getId() != null && transactionRepository.existsById(request.getId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_ID_EXIST));
            }
            TransactionCategory transactionCategory = transactionCategoryRepository.findById(request.getTransactionCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_CATEGORY_NOT_FOUND)));
            User userCreated = authHelper.getUser();

            Transaction newTransaction = transactionMapper.mapToEntity(request);
            newTransaction.setStatus(TransactionStatus.COMPLETED);
            newTransaction.setUserCreated(userCreated);
            newTransaction.setCategory(transactionCategory);

            transactionRepository.save(newTransaction);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Tự động tạo phiếu thu/chi dựa trên yêu cầu đầu vào mà không kiểm tra mã phiếu thu/chi.
     *
     * @param request Đối tượng chứa thông tin tạo phiếu thu/chi tự động.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> autoCreateTransaction(AutoCreateTransactionRequest request) {
        try {
            User userCreated = authHelper.getUser();

            Transaction newTransaction = transactionMapper.mapToEntity(request);
            newTransaction.setUserCreated(userCreated);

            transactionRepository.save(newTransaction);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lọc danh sách phiếu thu/chi theo tiêu chí, phân trang và sắp xếp.
     * Các trường dữ liệu sẽ được ánh xạ động dựa trên `filterParams`.
     *
     * @param request      Đối tượng chứa các tiêu chí lọc.
     * @param filterParams Map chứa các trường cần bao gồm hoặc loại bỏ.
     * @param sort         Hướng sắp xếp (ASC hoặc DESC).
     * @param sortField    Trường được sắp xếp.
     * @param page         Số trang hiện tại.
     * @param size         Số lượng mục trên một trang.
     * @return ResponseEntity chứa danh sách phiếu thu/chi đã lọc và thông tin phân trang.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> filterTransaction(GetListTransactionRequest request, Map<String, Boolean> filterParams, String sort, String sortField, int page, int size) {
        try {
            TransactionSpecification specification = new TransactionSpecification(request);
            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sort), sortField));

            Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);

            List<TransactionGetListResponse> responseList = transactionPage.stream().map(transaction -> {
                TransactionGetListResponse response = new TransactionGetListResponse();

                for (Map.Entry<String, Boolean> entry : filterParams.entrySet()) {
                    String field = entry.getKey();
                    Boolean includeField = entry.getValue();

                    // Ánh xạ các trường dựa trên filterParams
                    if (includeField) {
                        if (field.equals("user_created_name")) {
                            response.setUserCreatedName(transaction.getUserCreated().getFullName());
                        } else {
                            try {
                                // Lấy phương thức set tương ứng
                                Method setMethod = response.getClass().getMethod("set" + StringUtils.snakeCaseToCamelCase(field), String.class);

                                // Gọi phương thức set với giá trị tương ứng từ đối tượng transaction
                                setMethod.invoke(response, CommonUtils.getFieldValue(transaction, field));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                return response;
            }).toList();

            Pagination pagination = Pagination.<Object>builder()
                    .data(responseList)
                    .totalPage(transactionPage.getTotalPages())
                    .totalItems(transactionPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin phiếu thu/chi dựa trên mã phiếu thu/chi và yêu cầu đầu vào.
     * Nếu mã phiếu thu/chi không tồn tại, trả về lỗi.
     *
     * @param id      Mã phiếu thu/chi cần cập nhật.
     * @param request Đối tượng chứa thông tin cập nhật phiếu thu/chi.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> updateTransaction(String id, UpdateTransactionRequest request) {
        try {
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_FOUND)));

            if (request.getId() != null && !request.getId().equals(transaction.getId()) && transactionRepository.existsById(request.getId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.TRANSACTION_ID_EXIST));
            }

            transactionMapper.updateFromToDTO(request, transaction);

            transactionRepository.save(transaction);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Hủy bỏ phiếu thu/chi dựa trên mã phiếu thu/chi. Nếu phiếu thu/chi không thể hủy, ném ngoại lệ NoActionForOperationException.
     *
     * @param id Mã phiếu thu/chi cần hủy.
     * @return ResponseEntity chứa phản hồi thành công hoặc lỗi.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> cancelTransaction(String id) {
        try {
            Transaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_FOUND)));
            if (transaction.getCategory().getId().equals("TSC00001") || transaction.getCategory().getId().equals("TSC00002")) {
                throw new NoActionForOperationException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.TRANSACTION_NOT_CANCELABLE));
            }

            transaction.setStatus(TransactionStatus.CANCELLED);

            transactionRepository.save(transaction);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.TRANSACTION_CANCEL_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
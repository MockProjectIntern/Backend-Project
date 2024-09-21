package com.sapo.mock_project.inventory_receipt.dtos.internal.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Lớp DTO đại diện cho yêu cầu tự động tạo phiếu thu/chi.
 * Chứa thông tin cần thiết để tự động tạo một phiếu thu/chi mới.
 */
public class AutoCreateTransactionRequest {

    /**
     * Ghi chú hoặc mô tả về phiếu thu/chi.
     */
    @JsonProperty("note")
    @Schema(description = "Ghi chú hoặc mô tả về phiếu thu/chi", example = "Thanh toán hóa đơn tháng 9")
    private String note;

    /**
     * Mã tham chiếu của phiếu thu/chi.
     */
    @JsonProperty("reference_code")
    @Schema(description = "Mã tham chiếu của phiếu thu/chi", example = "REF123456")
    private String referenceCode;

    /**
     * ID tham chiếu của phiếu thu/chi.
     */
    @JsonProperty("reference_id")
    @Schema(description = "ID tham chiếu của phiếu thu/chi", example = "REFID001")
    private String referenceId;

    /**
     * Nhóm người nhận của phiếu thu/chi.
     */
    @JsonProperty("recipient_group")
    @Schema(description = "Nhóm người nhận của phiếu thu/chi", example = "Nhóm B")
    private String recipientGroup;

    /**
     * ID người nhận của phiếu thu/chi.
     */
    @JsonProperty("recipient_id")
    @Schema(description = "ID người nhận của phiếu thu/chi", example = "REC002")
    private String recipientId;

    /**
     * Loại phiếu thu/chi.
     */
    @JsonProperty("type")
    @Schema(description = "Loại phiếu thu/chi", example = "INCOME")
    private TransactionType type;
}

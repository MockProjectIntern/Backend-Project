package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.PriceAdjustmentStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.TransactionMethod;
import com.sapo.mock_project.inventory_receipt.constants.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Lớp DTO đại diện cho yêu cầu tạo mới phiếu thu/chi.
 * Chứa thông tin cần thiết để tạo một phiếu thu/chi mới.
 */
@Data
public class CreateTransactionRequest {

    /**
     * Mã phiếu thu/chi (ID) của phiếu thu/chi.
     * Trong trường hợp này, mã phiếu thu/chi có thể được hệ thống sinh tự động.
     */
    @JsonProperty("id")
    @Schema(description = "Mã phiếu thu/chi (ID) của phiếu thu/chi", example = "TX123456")
    private String id;

    /**
     * Số tiền của phiếu thu/chi.
     */
    @JsonProperty("new_price")
    @Schema(description = "Số tiền của phiếu thu/chi", example = "1000.00")
    private BigDecimal newPrice;

    /**
     * Phương thức thanh toán được sử dụng cho phiếu thu/chi.
     */
    @JsonProperty("status")
    @Schema(description = "Phương thức thanh toán được sử dụng cho phiếu thu/chi", example = "PriceAdjustmentStatus")
    private PriceAdjustmentStatus status;

    /**
     * Các tag liên quan đến phiếu thu/chi.
     */
    @JsonProperty("tags")
    @Schema(description = "Các tag liên quan đến phiếu thu/chi", example = "['urgent', 'office']")
    private String tags;

    /**
     * Ghi chú hoặc mô tả về phiếu thu/chi.
     */
    @JsonProperty("note")
    @Schema(description = "Ghi chú hoặc mô tả về phiếu thu/chi", example = "Thanh toán cho hóa đơn tháng 8")
    private String note;

    /**
     * Nhóm người nhận của phiếu thu/chi.
     */
//    @JsonProperty("user_cr")
//    @Schema(description = "Nhóm người nhận của phiếu thu/chi", example = "Nhóm A")
//    private String productId;

}

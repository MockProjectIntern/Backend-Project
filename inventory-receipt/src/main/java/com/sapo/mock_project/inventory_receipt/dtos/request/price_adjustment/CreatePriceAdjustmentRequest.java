package com.sapo.mock_project.inventory_receipt.dtos.request.price_adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.PriceAdjustmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO yêu cầu để tạo một nhà cung cấp mới.
 */
@Data
public class CreatePriceAdjustmentRequest {
    /**
     * ID của nhà cung cấp.
     * Trường này có thể không cần thiết khi tạo mới và thường được hệ thống tự động sinh ra.
     */
    @Schema(description = "ID của nhà cung cấp", example = "SUP12345", required = false)
    @JsonProperty("id")
    private String id;

    /**
     * ID của nhóm nhà cung cấp mà nhà cung cấp này thuộc về.
     * Đây là thông tin bắt buộc.
     */
    @Schema(description = "ID của nhóm nhà cung cấp mà nhà cung cấp này thuộc về", example = "GROUP001", required = true)
    @JsonProperty("product_id")
    private String productId;

    /**
     * Các thẻ hoặc tag liên quan đến nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Các thẻ hoặc tag liên quan đến nhà cung cấp", example = "Nhà cung cấp chính, Ưu tiên cao", required = false)
    @JsonProperty("tags")
    private String tags;

    /**
     * Ghi chú về nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín", required = false)
    @JsonProperty("note")
    private String note;

    /**
     * Ghi chú về nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín", required = false)
    @JsonProperty("status")
    private PriceAdjustmentStatus status;

    /**
     * Ghi chú về nhà cung cấp.
     * Đây là thông tin tùy chọn.
     */
    @Schema(description = "Ghi chú về nhà cung cấp", example = "Nhà cung cấp lâu năm, uy tín", required = false)
    @JsonProperty("new_price")
    private BigDecimal newPrice;
}

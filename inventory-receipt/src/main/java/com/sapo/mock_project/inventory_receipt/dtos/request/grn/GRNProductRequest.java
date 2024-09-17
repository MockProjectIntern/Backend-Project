package com.sapo.mock_project.inventory_receipt.dtos.request.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO yêu cầu cho sản phẩm trong phiếu nhập kho (GRN).
 */
@Data
public class GRNProductRequest {

    /**
     * ID của sản phẩm.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("id")
    @Schema(description = "ID của sản phẩm", example = "PROD12345", required = true)
    private String id;

    /**
     * Số lượng của sản phẩm.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("quantity")
    @Schema(description = "Số lượng của sản phẩm", example = "10", required = true)
    private BigDecimal quantity;

    /**
     * Giá của sản phẩm.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("price")
    @Schema(description = "Giá của sản phẩm", example = "150000.00", required = true)
    private BigDecimal price;

    /**
     * Chiết khấu áp dụng cho sản phẩm (nếu có).
     * Đây là thông tin tùy chọn.
     */
    @JsonProperty("discount")
    @Schema(description = "Chiết khấu của sản phẩm", example = "5000.00", required = false)
    private BigDecimal discount;

    /**
     * Thuế áp dụng cho sản phẩm.
     * Đây là thông tin bắt buộc.
     */
    @JsonProperty("tax")
    @Schema(description = "Thuế của sản phẩm", example = "10000.00", required = true)
    private BigDecimal tax;
}

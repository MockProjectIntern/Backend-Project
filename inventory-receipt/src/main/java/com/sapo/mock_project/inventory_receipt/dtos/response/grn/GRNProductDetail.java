package com.sapo.mock_project.inventory_receipt.dtos.response.grn;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO chứa thông tin chi tiết về sản phẩm trong phiếu nhập kho (GRN).
 */
@Data
public class GRNProductDetail {

    /**
     * ID của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "ID của sản phẩm trong phiếu nhập kho", example = "PROD12345")
    @JsonProperty("id")
    private String id;

    /**
     * Số lượng của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Số lượng của sản phẩm trong phiếu nhập kho", example = "50")
    @JsonProperty("quantity")
    private BigDecimal quantity;

    /**
     * Chiết khấu áp dụng cho sản phẩm.
     */
    @Schema(description = "Chiết khấu áp dụng cho sản phẩm", example = "500.00")
    @JsonProperty("discount")
    private BigDecimal discount;

    /**
     * Số tiền thuế áp dụng cho sản phẩm.
     */
    @Schema(description = "Số tiền thuế áp dụng cho sản phẩm", example = "1000.00")
    @JsonProperty("tax")
    private BigDecimal tax;

    /**
     * Giá của sản phẩm trong phiếu nhập kho.
     */
    @Schema(description = "Giá của sản phẩm trong phiếu nhập kho", example = "10000.00")
    @JsonProperty("price")
    private BigDecimal price;

}

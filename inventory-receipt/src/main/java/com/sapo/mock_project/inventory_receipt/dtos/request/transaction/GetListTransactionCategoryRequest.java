package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu lấy danh sách danh mục giao dịch.
 * <p>
 * Lớp này chứa các thông tin cần thiết để lọc và tìm kiếm danh mục giao dịch, bao gồm từ khóa tìm kiếm và loại giao dịch.
 * </p>
 */
@Data
@Schema(description = "Yêu cầu lấy danh sách danh mục giao dịch")
public class GetListTransactionCategoryRequest {

    /**
     * Từ khóa tìm kiếm để lọc danh mục giao dịch.
     * <p>
     * Đây là chuỗi ký tự dùng để tìm kiếm trong tên và mô tả của danh mục giao dịch.
     * </p>
     */
    @JsonProperty("keyword")
    @Schema(description = "Từ khóa tìm kiếm để lọc danh mục giao dịch", example = "Danh mục")
    private String keyword;

    /**
     * Loại giao dịch để lọc danh mục giao dịch.
     * <p>
     * Đây là loại giao dịch được định nghĩa bởi {@link TransactionType}, dùng để phân loại danh mục giao dịch.
     * </p>
     */
    @JsonProperty("type")
    @Schema(description = "Loại giao dịch để lọc danh mục giao dịch", example = "PURCHASE")
    private TransactionType type;
}

package com.sapo.mock_project.inventory_receipt.dtos.request.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu tạo một danh mục giao dịch mới.
 * <p>
 * Lớp này chứa các thông tin cần thiết để tạo một danh mục giao dịch mới bao gồm ID, tên, mô tả, và loại giao dịch.
 * </p>
 */
@Data
@Schema(description = "Yêu cầu tạo một danh mục giao dịch mới")
public class CreateTransactionCategoryRequest {

    /**
     * ID của danh mục giao dịch.
     * <p>
     * Đây là mã định danh duy nhất của danh mục giao dịch, có thể được sử dụng để nhận diện danh mục trong cơ sở dữ liệu.
     * </p>
     */
    @JsonProperty("id")
    @Schema(description = "ID của danh mục giao dịch", example = "12345")
    private String id;

    /**
     * Tên của danh mục giao dịch.
     * <p>
     * Đây là tên của danh mục giao dịch được sử dụng để nhận diện và phân loại danh mục.
     * </p>
     */
    @JsonProperty("name")
    @Schema(description = "Tên của danh mục giao dịch", example = "Danh mục 1")
    private String name;

    /**
     * Mô tả của danh mục giao dịch.
     * <p>
     * Đây là mô tả chi tiết về danh mục giao dịch, cung cấp thêm thông tin về danh mục.
     * </p>
     */
    @JsonProperty("description")
    @Schema(description = "Mô tả của danh mục giao dịch", example = "Mô tả chi tiết về danh mục 1")
    private String description;

    /**
     * Loại giao dịch của danh mục.
     * <p>
     * Đây là loại giao dịch mà danh mục thuộc về, được định nghĩa bởi {@link TransactionType}.
     * </p>
     */
    @JsonProperty("type")
    @Schema(description = "Loại giao dịch của danh mục", example = "PURCHASE")
    private TransactionType type;
}

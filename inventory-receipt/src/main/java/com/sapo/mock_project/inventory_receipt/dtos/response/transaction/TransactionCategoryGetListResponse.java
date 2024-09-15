package com.sapo.mock_project.inventory_receipt.dtos.response.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO đại diện cho phản hồi khi lấy danh sách danh mục giao dịch.
 * <p>
 * Lớp này kế thừa từ {@link BaseResponse} và chứa các thông tin cơ bản của danh mục giao dịch
 * bao gồm ID, tên, và mô tả.
 * </p>
 */
@Data
@Schema(description = "Phản hồi khi lấy danh sách danh mục giao dịch")
public class TransactionCategoryGetListResponse extends BaseResponse {

    /**
     * ID của danh mục giao dịch.
     * <p>
     * Đây là một chuỗi đại diện cho mã định danh duy nhất của danh mục giao dịch.
     * </p>
     */
    @JsonProperty("id")
    @Schema(description = "ID của danh mục giao dịch", example = "12345")
    private String id;

    /**
     * Tên của danh mục giao dịch.
     * <p>
     * Đây là tên của danh mục giao dịch được sử dụng để nhận diện và phân loại.
     * </p>
     */
    @JsonProperty("name")
    @Schema(description = "Tên của danh mục giao dịch", example = "Danh mục 1")
    private String name;

    /**
     * Mô tả của danh mục giao dịch.
     * <p>
     * Đây là mô tả chi tiết về danh mục giao dịch, cung cấp thêm thông tin về nó.
     * </p>
     */
    @JsonProperty("description")
    @Schema(description = "Mô tả của danh mục giao dịch", example = "Mô tả chi tiết về danh mục 1")
    private String description;
}

package com.sapo.mock_project.inventory_receipt.dtos.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO phản hồi cho danh sách danh mục.
 * Đây là đối tượng trả về khi lấy thông tin về danh mục từ hệ thống.
 */
@Data
@Schema(description = "Thông tin phản hồi danh mục")
public class GetListCategoryRequest extends BaseResponse {

    /**
     * ID của danh mục.
     *
     * @return ID của danh mục.
     */
    @JsonProperty("id")
    @Schema(description = "ID của danh mục", example = "123")
    private String id;

    /**
     * ID của danh mục phụ (nếu có).
     *
     * @return ID của danh mục phụ.
     */
    @JsonProperty("sub_id")
    @Schema(description = "ID của danh mục phụ", example = "456")
    private String subId;

    /**
     * Tên của danh mục.
     *
     * @return Tên của danh mục.
     */
    @JsonProperty("name")
    @Schema(description = "Tên của danh mục", example = "Điện thoại")
    private String name;
}

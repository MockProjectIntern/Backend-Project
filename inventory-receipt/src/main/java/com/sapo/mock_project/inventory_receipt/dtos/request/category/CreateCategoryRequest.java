package com.sapo.mock_project.inventory_receipt.dtos.request.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO dùng để nhận yêu cầu tạo mới một danh mục.
 * Được sử dụng để gửi thông tin của danh mục mới cần tạo đến server.
 */
@Data
@Schema(description = "Thông tin yêu cầu để tạo mới danh mục")
public class CreateCategoryRequest {

    /**
     * Tên của danh mục cần tạo.
     *
     * @return Tên của danh mục.
     */
    @JsonProperty("name")
    @NotBlank(message = "Tên danh mục không được để trống")
    @Schema(description = "Tên của danh mục", example = "Điện thoại", required = true)
    private String name;
}

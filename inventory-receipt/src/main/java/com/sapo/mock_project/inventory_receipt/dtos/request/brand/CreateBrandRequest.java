package com.sapo.mock_project.inventory_receipt.dtos.request.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO yêu cầu tạo mới thương hiệu.
 */
@Data
public class CreateBrandRequest {

    @Schema(description = "Tên thương hiệu", example = "Thương hiệu A")
    @JsonProperty("name")
    private String name;
}

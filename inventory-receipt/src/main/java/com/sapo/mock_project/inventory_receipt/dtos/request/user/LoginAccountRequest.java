package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * DTO dùng để yêu cầu đăng nhập của người dùng.
 * Chứa thông tin cần thiết để thực hiện đăng nhập.
 */
@Data
@Builder
public class LoginAccountRequest {

    /**
     * Số điện thoại của người dùng.
     * Trường này là bắt buộc và phải khớp với số điện thoại đã đăng ký.
     */
    @Schema(description = "Số điện thoại của người dùng", required = true, example = "0123456789")
    @JsonProperty("phone")
    private String phone;

    /**
     * Mật khẩu của người dùng.
     * Trường này là bắt buộc và phải khớp với mật khẩu đã đăng ký.
     */
    @Schema(description = "Mật khẩu của người dùng", required = true, example = "password123")
    @JsonProperty("password")
    private String password;
}

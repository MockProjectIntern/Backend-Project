package com.sapo.mock_project.inventory_receipt.dtos.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterAccountRequest {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private RoleEnum role;
}

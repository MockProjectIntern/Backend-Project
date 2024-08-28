package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class ResponseObject<T> {
    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;
}

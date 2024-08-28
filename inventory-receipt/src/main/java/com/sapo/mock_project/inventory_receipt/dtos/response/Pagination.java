package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Pagination<T> {
    @JsonProperty("data")
    private T data;

    @JsonProperty("page")
    private int totalPage;

    @JsonProperty("total_items")
    private long totalItems;
}
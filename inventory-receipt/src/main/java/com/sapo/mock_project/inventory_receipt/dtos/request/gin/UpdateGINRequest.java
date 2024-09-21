package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGINRequest {
    @JsonProperty("note")
    private String note;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("user_inspection_id")
    private String userInspectionId;

    @JsonProperty("products")
    List<UpdateGINProductRequest> products;

    @JsonProperty("is_balance")
    private boolean isBalance;
}

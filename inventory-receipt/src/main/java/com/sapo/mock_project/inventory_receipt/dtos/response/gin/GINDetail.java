package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.BaseResponse;
import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GINDetail extends BaseResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sub_id")
    private String subId;

    @JsonProperty("status")
    private GINStatus status;

    @JsonProperty("balanced_at")
    private LocalDate balancedAt;

    @JsonProperty("user_created_name")
    private String userCreatedName;

    @JsonProperty("user_balanced_name")
    private String userBalancedName;

    @JsonProperty("user_inspection_name")
    private String userInspectionName;

    @JsonProperty("tags")
    private String tags;

    @JsonProperty("note")
    private String note;

    @JsonProperty("products")
    private List<GINProduct> products;
}

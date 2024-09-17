package com.sapo.mock_project.inventory_receipt.dtos.response.gin;

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
    private String id;

    private GINStatus status;

    private LocalDate balancedAt;
    private String userCreatedName;
    private String userBalancedName;
    private String userInspectionName;

    private String tags;
    private String note;
    private List<GINProduct> products;
}

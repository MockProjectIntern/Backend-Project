package com.sapo.mock_project.inventory_receipt.dtos.request.gin;

import com.sapo.mock_project.inventory_receipt.entities.GINProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGINRequest {

    private String note;
    private String tags;

    private String userInspectionId;

    List<GINProduct> products;
}

package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNHistory {
    private LocalDateTime date;

    private String userExecuted;

    private String function;

    private String operation;
}

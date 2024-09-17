package com.sapo.mock_project.inventory_receipt.entities.subentities;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GRNHistory {
    private LocalDateTime date;
    private String user_executed;
    private String function;
    private String operation;
    @Override
    public String toString() {
        return "GRNHistory{" +
                "date=" + date +
                ", user_executed='" + user_executed + '\'' +
                ", function='" + function + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}

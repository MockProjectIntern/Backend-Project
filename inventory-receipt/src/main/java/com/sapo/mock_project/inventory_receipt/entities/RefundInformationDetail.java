package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "refund_information_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundInformationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal quantity;

    private BigDecimal refundedPrice;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "refund_information_id")
    private RefundInformation refundInformation;

    @ManyToOne
    @JoinColumn(name = "grn_product_id")
    private PriceAdjustment priceAdjustment;
}

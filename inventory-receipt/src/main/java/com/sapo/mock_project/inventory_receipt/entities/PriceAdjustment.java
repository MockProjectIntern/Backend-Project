package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.PriceAdjustmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "price_adjustments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PriceAdjustmentStatus status;

    private String note;

    private String tags;

    private BigDecimal newPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @OneToMany(mappedBy = "priceAdjustment")
    private List<RefundInformationDetail> refundInformationDetails;
}

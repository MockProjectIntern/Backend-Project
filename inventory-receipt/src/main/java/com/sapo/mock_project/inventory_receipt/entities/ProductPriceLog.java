package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.ProductPriceLogStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_price_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ProductPriceLogStatus status;

    private String note;

    private String tags;

    private BigDecimal priceDifference;

    private BigDecimal priceAdjusted;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

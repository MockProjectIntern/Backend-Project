package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.PriceAdjustmentStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "price_adjustments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceAdjustment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_adjustment_sequences")
    @GenericGenerator(
            name = "price_adjustment_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.PRICE_ADJUSTMENT),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "price_adjustment_sequences")
            })
    private String id;

    private String subId;

    @Enumerated(EnumType.STRING)
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

    @Override
    protected void customPrePersist() {
        if (subId == null && id != null) {
            subId = id;
        }
    }
}

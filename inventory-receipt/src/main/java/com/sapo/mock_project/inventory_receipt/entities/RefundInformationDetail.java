package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "refund_information_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundInformationDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_information_detail_sequences")
    @GenericGenerator(
            name = "refund_information_detail_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.REFUND_INFORMATION_DETAIL),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "refund_information_detail_sequences")
            })
    private String id;

    private String subId;

    private BigDecimal quantity;

    private BigDecimal refundedPrice;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "refund_information_id")
    private RefundInformation refundInformation;

    @ManyToOne
    @JoinColumn(name = "grn_product_id")
    private PriceAdjustment priceAdjustment;

    @Override
    protected void customPrePersist() {
        if (subId == null && id != null) {
            subId = id;
        }
    }
}

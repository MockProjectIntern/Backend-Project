package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundInformationStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundPaymentStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "refund_informations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundInformation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_information_sequences")
    @GenericGenerator(
            name = "refund_information_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.REFUND_INFORMATION),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "refund_information_sequences")
            })
    private String id;

    private String subId;

    private BigDecimal totalRefundedQuantity;

    private BigDecimal totalRefundedValue;

    private BigDecimal totalRefundedCost;

    private BigDecimal totalRefundTax;

    private BigDecimal totalRefundDiscount;

    private String reason;

    private String refundType;

    private BigDecimal refundAmount;

    private RefundInformationStatus status;

    private RefundPaymentStatus refundPaymentStatus;

    private LocalDate refundReceivedDate;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "grn_id")
    private GRN grn;

    @OneToMany(mappedBy = "refundInformation")
    private List<RefundInformationDetail> refundInformationDetails;

    @Override
    protected void customPrePersist() {
        if (subId == null && id != null) {
            subId = id;
        }
    }
}

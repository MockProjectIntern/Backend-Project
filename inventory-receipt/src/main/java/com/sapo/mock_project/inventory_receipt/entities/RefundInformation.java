package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.RefundInformationStatus;
import com.sapo.mock_project.inventory_receipt.constants.enums.RefundPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

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
public class RefundInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}

package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.converts.GRNImportCostConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNPaymentMethodConverter;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grns")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GRN extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grn_sequences")
    @GenericGenerator(
            name = "grn_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "GRN"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "grn_sequences")
            })
    private String id;

    private GRNStatus status;

    private GRNReceiveStatus receiveStatus;

    private LocalDate expectedDeliveryAt;

    private LocalDate receivedAt;

    private LocalDate cancelledAt;

    private LocalDateTime paymentAt;

    private BigDecimal totalReceivedQuantity;

    private BigDecimal discount;

    private BigDecimal taxAmount;

    private BigDecimal totalValue;

    private GRNPaymentStatus paymentStatus;

    private ReturnStatus returnStatus;

    private GRNRefundStatus refundStatus;

    private String note;

    private String tags;

    @Convert(converter = GRNImportCostConverter.class)
    private GRNImportCost importCost;

    @Convert(converter = GRNPaymentMethodConverter.class)
    private GRNPaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_imported_id")
    private User userImported;

    @ManyToOne
    @JoinColumn(name = "user_cancelled_id")
    private User userCancelled;

    @ManyToOne
    @JoinColumn(name = "user_ended_id")
    private User userEnded;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "grn")
    private List<RefundInformation> refundInformations;

    @OneToMany(mappedBy = "grn")
    private List<GRNProduct> grnProducts;
}

package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.converts.GRNImportCostConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNPaymentMethodConverter;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import jakarta.persistence.*;
import lombok.*;

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
public class GRN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GRNStatus status;

    private GRNReceiveStatus receiveStatus;

    private LocalDate expectedDeliveryAt;

    private LocalDate receivedAt;

    private LocalDate cancelledAt;

    private LocalDateTime paymentAt;

    private BigDecimal totalReceivedQuantity;

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

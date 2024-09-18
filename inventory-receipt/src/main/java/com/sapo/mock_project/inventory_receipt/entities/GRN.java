package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.PrefixId;
import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.converts.GRNHistoryConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNImportCostConverter;
import com.sapo.mock_project.inventory_receipt.converts.GRNPaymentMethodConverter;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNHistory;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNImportCost;
import com.sapo.mock_project.inventory_receipt.entities.subentities.GRNPaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GRN extends BaseEntity {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grn_sequences")
    @GenericGenerator(
            name = "grn_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = PrefixId.GRN),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "grn_sequences")
            })
    private String id;

    private String subId;

    @Enumerated(EnumType.STRING)
    private GRNStatus status;

    @Enumerated(EnumType.STRING)
    private GRNReceiveStatus receivedStatus;

    private LocalDate expectedDeliveryAt;

    private LocalDate receivedAt;

    private LocalDate endedAt;

    private LocalDate cancelledAt;

    private LocalDateTime paymentAt;

    private BigDecimal totalReceivedQuantity;

    private BigDecimal discount;

    private BigDecimal taxAmount;

    private BigDecimal totalValue;

    private BigDecimal totalPaid;

    @Enumerated(EnumType.STRING)
    private GRNPaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus;

    @Enumerated(EnumType.STRING)
    private GRNRefundStatus refundStatus;

    private String note;

    private String tags;

    @Convert(converter = GRNImportCostConverter.class)
    @Column(name = "import_costs")
    private List<GRNImportCost> importCosts;

    @Convert(converter = GRNPaymentMethodConverter.class)
    @Column(name = "payment_methods")
    private List<GRNPaymentMethod> paymentMethods;

    @Convert(converter = GRNHistoryConverter.class)
    @Column(name = "histories")
    private List<GRNHistory> histories;

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

    public void calculatorValue() {
        if (totalReceivedQuantity == null) {
            totalReceivedQuantity = BigDecimal.ZERO;
        }
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            taxAmount = BigDecimal.ZERO;
        }

        if (totalPaid == null) {
            totalPaid = BigDecimal.ZERO;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal preTotalPaid = totalPaid;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal calPaidNow = BigDecimal.ZERO;

        // Tính toán tổng số lượng và tổng thuế cho từng sản phẩm trong danh sách
        for (GRNProduct grnProduct : grnProducts) {
            log.info("GRN product: {} ", grnProduct.toString());
            totalPrice = totalPrice.add(grnProduct.calculateTotal());
            totalQuantity = totalQuantity.add(grnProduct.getQuantity());
            totalTax = totalTax.add(grnProduct.getTax().multiply(grnProduct.getQuantity()));
        }

        totalReceivedQuantity = totalQuantity;
        taxAmount = totalTax;
        totalValue = totalPrice.subtract(discount);

        // Tính toán tổng chi phí nhập khẩu (nếu có)
        if (importCosts != null && !importCosts.isEmpty()) {
            for (GRNImportCost importCost : importCosts) {
                log.info("Import cost: {} ", importCost.toString());
                totalValue = totalValue.add(importCost.getValue());
            }
        }
        log.info("Total value: {} , Total tax : {} , Total receive quantity: {}", totalValue, totalTax, totalQuantity);

        // Tính toán tổng số tiền đã thanh toán từ các phương thức thanh toán
        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            for (GRNPaymentMethod paymentMethod : paymentMethods) {
                log.info("Payment method: {} ", paymentMethod.toString());
                BigDecimal money = paymentMethod.getAmount();
                if (money.compareTo(BigDecimal.ZERO) > 0) {
                    if (calPaidNow.add(money).compareTo(totalValue) > 0) {
                        throw new RuntimeException("Total paid must be less than or equal to total value");
                    }
                    // Cập nhật giá trị calPaidNow với số tiền mới
                    calPaidNow = calPaidNow.add(money);
                    log.info("Updated paid amount: {} ", calPaidNow);
                }
            }
        }
        totalPaid = calPaidNow;

        // Xác định trạng thái thanh toán dựa trên tổng số tiền đã trả và tổng giá trị
        if (totalPaid.compareTo(totalValue) == 0) {
            paymentStatus = GRNPaymentStatus.PAID;
            status = GRNStatus.COMPLETED;
            paymentAt = LocalDateTime.now();
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            paymentStatus = GRNPaymentStatus.PARTIAL_PAID;
            if (preTotalPaid.compareTo(totalPaid) < 0) {
                paymentAt = LocalDateTime.now();
            }
        } else {
            paymentStatus = GRNPaymentStatus.UNPAID;
        }

        // Xử lý trạng thái hoàn trả và hoàn tiền
        if (refundInformations == null) {
            refundStatus = GRNRefundStatus.NOT_REFUNDED;
            returnStatus = ReturnStatus.NOT_RETURNED;
        }
    }

    @Override
    protected void customPrePersist() {
        if (subId == null && id != null) {
            subId = id;
        }
    }
}

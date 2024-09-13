package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_sequences")
    @GenericGenerator(
            name = "order_detail_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "ORDD"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "order_detail_sequences")
            })
    private String id;

    private Long quantity;

    private BigDecimal price;

    private BigDecimal discount;

    private BigDecimal importedQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}

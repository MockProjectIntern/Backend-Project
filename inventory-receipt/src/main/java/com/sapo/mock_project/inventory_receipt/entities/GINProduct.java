package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "gin_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GINProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin_product_sequences")
    @GenericGenerator(
            name = "gin_product_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "GINP"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "gin_product_sequences")
            })
    private String id;

    private BigDecimal actualStock;

    private BigDecimal discrepancyQuantity;

    private String reason;

    private String note;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "gin_id")
    private GIN gin;
}

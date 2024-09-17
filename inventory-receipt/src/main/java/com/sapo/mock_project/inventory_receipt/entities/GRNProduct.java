package com.sapo.mock_project.inventory_receipt.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "grn_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GRNProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grn_product_sequences")
    @GenericGenerator(
            name = "grn_product_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "GRNP"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "grn_product_sequences")
            })
    private String id;

    private BigDecimal quantity;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "grn_id")
    private GRN grn;

    @Transient
    @JsonProperty("product_id")
    private String productId;

    public BigDecimal calculateTotal() {
        return (price.multiply(quantity)).subtract(discount).add(tax);
    }

    @Override
    public String toString() {
        return "GRNProduct{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", discount=" + discount +
                ", tax=" + tax +
                ", price=" + price +
                ", product=" + product +
                ", grn=" + grn +
                ", productId='" + productId + '\'' +
                '}';
    }
}

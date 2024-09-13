package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "category_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_product_sequences")
    @GenericGenerator(
            name = "category_product_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "CAP"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "category_product_sequences")
            })
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

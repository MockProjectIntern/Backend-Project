package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "supplier_groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_group_sequences")
    @GenericGenerator(
            name = "supplier_group_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "SUPGR"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "supplier_group_sequences")
            })
    private String id;

    private String name;

    private String note;
}

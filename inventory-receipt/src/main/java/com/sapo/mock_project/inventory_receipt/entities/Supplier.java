package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_sequences")
    @GenericGenerator(
            name = "supplier_sequences",
            strategy = "com.sapo.mock_project.inventory_receipt.entities.sequence.StringPrefixSequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "SUP"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d"),
                    @org.hibernate.annotations.Parameter(name = StringPrefixSequenceGenerator.SEQUENCE_TABLE_PARAMETER, value = "supplier_sequences")
            })
    private String id;

    private String name;

    private String phone;

    private String email;

    private String address;

    private SupplierStatus status;

    @ManyToOne
    @JoinColumn(name = "supplier_group_id")
    private SupplierGroup group;

    @OneToMany(mappedBy = "supplier")
    private List<GRN> grns;
}

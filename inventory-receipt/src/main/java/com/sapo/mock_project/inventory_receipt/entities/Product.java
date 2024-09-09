package com.sapo.mock_project.inventory_receipt.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String image;

    private String brand;

    private String variant;

    private String color;

    private Long quantity;

    private Long sold;

    private String description;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "product")
    private List<ProductReceipt> productReceipts;

    @OneToMany(mappedBy = "product")
    private List<CategoryProduct> categoryProducts;
}

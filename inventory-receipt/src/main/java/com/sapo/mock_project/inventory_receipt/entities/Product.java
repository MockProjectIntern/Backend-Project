package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.converts.ProductImageConverter;
import com.sapo.mock_project.inventory_receipt.converts.ProductTypeConverter;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductImage;
import com.sapo.mock_project.inventory_receipt.entities.subentities.ProductType;
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

    @Convert(converter = ProductImageConverter.class)
    private List<ProductImage> images;

    @Convert(converter = ProductTypeConverter.class)
    private List<ProductType> types;

    private BigDecimal quantity;

    private BigDecimal sold;

    private String description;

    private String unit;

    private BigDecimal costPrice;

    private BigDecimal wholesalePrice;

    private BigDecimal retailPrice;

    private String tags;

    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product")
    private List<GINProduct> gins;

    @OneToMany(mappedBy = "product")
    private List<CategoryProduct> categoryProducts;

    @OneToMany(mappedBy = "product")
    private List<PriceAdjustment> priceAdjustments;

    @OneToMany(mappedBy = "product")
    private List<GRNProduct> grnProducts;
}

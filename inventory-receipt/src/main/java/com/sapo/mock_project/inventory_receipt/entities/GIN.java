package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.GINStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "gins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GIN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GINStatus status;

    private LocalDate balancedAt;

    private String note;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_balanced_id")
    private User userBalanced;

    @ManyToOne
    @JoinColumn(name = "user_inspection_id")
    private User userInspection;

    @OneToMany(mappedBy = "gin")
    private List<GINProduct> products;
}

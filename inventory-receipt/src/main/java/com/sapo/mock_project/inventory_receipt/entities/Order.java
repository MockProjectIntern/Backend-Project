package com.sapo.mock_project.inventory_receipt.entities;

import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String note;

    private String tags;

    private LocalDate expectedAt;

    private LocalDate completedAt;

    private LocalDate endedAt;

    private LocalDate canceledAt;

    @ManyToOne
    @JoinColumn(name = "user_created_id")
    private User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_completed_id")
    private User userCompleted;

    @ManyToOne
    @JoinColumn(name = "user_canceled_id")
    private User userCancelled;

    @ManyToOne
    @JoinColumn(name = "user_ended_id")
    private User userEnded;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "order")
    private List<GRN> grns;
}

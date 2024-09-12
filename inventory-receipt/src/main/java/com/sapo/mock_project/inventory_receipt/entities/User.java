package com.sapo.mock_project.inventory_receipt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sapo.mock_project.inventory_receipt.constants.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String phone;

    private String email;

    @JsonIgnore
    private String password;

    private short gender;

    private String avatar;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @OneToMany(mappedBy = "userCreated")
    private List<GIN> createdGIN;

    @OneToMany(mappedBy = "userBalanced")
    private List<GIN> balancedGIN;

    @OneToMany(mappedBy = "userInspection")
    private List<GIN> inspectionGIN;

    @OneToMany(mappedBy = "userCreated")
    private List<Order> createdOrders;

    @OneToMany(mappedBy = "userCompleted")
    private List<Order> completedOrders;

    @OneToMany(mappedBy = "userCancelled")
    private List<Order> cancelledOrders;

    @OneToMany(mappedBy = "userEnded")
    private List<Order> endedOrders;

    @OneToMany(mappedBy = "userCreated")
    private List<PriceAdjustment> createdPriceAdjustments;

    @OneToMany(mappedBy = "userCreated")
    private List<RefundInformation> createdRefundInformations;

    @OneToMany(mappedBy = "userCreated")
    private List<GRN> createdGRNs;

    @OneToMany(mappedBy = "userImported")
    private List<GRN> importedGRNs;

    @OneToMany(mappedBy = "userCancelled")
    private List<GRN> cancelledGRNs;

    @OneToMany(mappedBy = "userEnded")
    private List<GRN> endedGRNs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole()));

        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }
}

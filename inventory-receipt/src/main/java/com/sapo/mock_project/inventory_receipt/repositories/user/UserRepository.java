package com.sapo.mock_project.inventory_receipt.repositories.user;

import com.sapo.mock_project.inventory_receipt.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByPhoneAndTenantId(String username, String tenantId);

    Optional<User> findByPhone(String phone);

    Optional<User> findByIdAndTenantId(String id, String tenantId);

    boolean existsByPhoneAndTenantId(String phone, String tenantId);

    boolean existsByPhone(String phone);

    @Query("SELECT u.id, u.fullName FROM User u WHERE u.tenantId = :tenantId")
    Page<Object[]> findAllFullNameAndTenantId(String tenantId, Pageable pageable);
}

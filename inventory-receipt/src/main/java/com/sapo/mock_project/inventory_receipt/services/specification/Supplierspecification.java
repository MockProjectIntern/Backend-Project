package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.SupplierStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.supplier.GetListSupplierRequest;
import com.sapo.mock_project.inventory_receipt.entities.Supplier;
import com.sapo.mock_project.inventory_receipt.utils.DateUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp định nghĩa các điều kiện lọc cho đối tượng {@link Supplier} sử dụng JPA Criteria API.
 * Các điều kiện lọc được dựa trên thông tin từ đối tượng {@link GetListSupplierRequest}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplierspecification implements Specification<Supplier> {

    /**
     * Đối tượng chứa các thông tin lọc.
     */
    private GetListSupplierRequest request;

    /**
     * Tạo ra các predicate (điều kiện) lọc dựa trên các tham số từ {@link GetListSupplierRequest}.
     *
     * @param root            Cơ sở của truy vấn, đại diện cho đối tượng {@link Supplier}.
     * @param query           Truy vấn mà các điều kiện sẽ được áp dụng.
     * @param criteriaBuilder Công cụ để xây dựng các điều kiện lọc.
     * @return Điều kiện lọc được kết hợp thành một điều kiện duy nhất.
     */
    @Override
    public Predicate toPredicate(Root<Supplier> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Lấy các thông tin lọc từ đối tượng request
        String keyword = request.getKeyword();
        SupplierStatus status = request.getStatus();
        String supplierGroupId = request.getSupplierGroupId();
        LocalDateTime createdDate = DateUtils.getDateTime(request.getCreatedDate());
        String tags = request.getTags();

        // Điều kiện lọc theo từ khóa
        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper));

            Predicate phonePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("phone")),
                    String.format("%%%s%%", keywordUpper));

            Predicate idPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("id")),
                    String.format("%%%s%%", keywordUpper));

            predicates.add(criteriaBuilder.or(namePredicate, phonePredicate, idPredicate));
        }

        // Loại trừ các nhà cung cấp đã bị xóa
        predicates.add(criteriaBuilder.notEqual(root.get("status"), SupplierStatus.DELETED));

        // Điều kiện lọc theo trạng thái
        if (status != null) {
            Predicate customStatus = criteriaBuilder.equal(root.get("status"), status);
            predicates.add(customStatus);
        }

        // Điều kiện lọc theo nhóm nhà cung cấp
        if (supplierGroupId != null && !supplierGroupId.isEmpty()) {
            Predicate customSupplierGroupId = criteriaBuilder.equal(root.get("group").get("id"), supplierGroupId);
            predicates.add(customSupplierGroupId);
        }

        // Điều kiện lọc theo ngày tạo
        if (createdDate != null) {
            Predicate customCreatedDate = criteriaBuilder.equal(root.get("createdAt"), createdDate);
            predicates.add(customCreatedDate);
        }

        // Điều kiện lọc theo tags
        if (tags != null && !tags.isEmpty()) {
            Predicate customTags = criteriaBuilder.like(criteriaBuilder.upper(root.get("tags")),
                    String.format("%%%s%%", tags.toUpperCase()));
            predicates.add(customTags);
        }

        // Kết hợp tất cả các điều kiện bằng phép AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

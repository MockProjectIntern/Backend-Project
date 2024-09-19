package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.constants.enums.ProductStatus;
import com.sapo.mock_project.inventory_receipt.dtos.request.product.GetListProductRequest;
import com.sapo.mock_project.inventory_receipt.entities.Product;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecification implements Specification<Product> {
    private GetListProductRequest request;


    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        List<String> categoryIds = request.getCategoryIds();
        List<String> brandIds = request.getBrandIds();
        List<ProductStatus> statuses = request.getStatuses();
        String tags = request.getTags();
        LocalDateTime createdDateFrom = DateUtils.getDateTimeFrom(request.getCreatedDateFrom());
        LocalDateTime createdDateTo = DateUtils.getDateTimeTo(request.getCreatedDateTo());

        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicates.add(root.get("category").get("id").in(categoryIds));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            predicates.add(root.get("brand").get("id").in(brandIds));
        }

        if (statuses != null && !statuses.isEmpty()) {
            predicates.add(root.get("status").in(statuses));
        }

        if (tags != null && !tags.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("tags"), String.format("%%%s%%", tags)));
        }

        if (createdDateFrom != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), createdDateFrom));
        }

        if (createdDateTo != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), createdDateTo));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

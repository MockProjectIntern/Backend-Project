package com.sapo.mock_project.inventory_receipt.services.specification;

import com.sapo.mock_project.inventory_receipt.dtos.request.category.GetListCategoryRequest;
import com.sapo.mock_project.inventory_receipt.entities.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySpecification implements Specification<Category> {
    private GetListCategoryRequest request;

    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();

        if (keyword != null && !keyword.isEmpty()) {
            String keywordUpper = keyword.toUpperCase();

            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),
                    String.format("%%%s%%", keywordUpper));

            predicates.add(criteriaBuilder.or(namePredicate));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

package org.example.repo.specification;

import org.example.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> searchByFilterText(String filterText) {
        return (root, query, criteriaBuilder) -> {
            if (filterText == null || filterText.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String filterTextLowerCase = "%" + filterText.toLowerCase() + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    filterTextLowerCase
            );
        };
    }
}

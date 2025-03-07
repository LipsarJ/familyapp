package org.example.repo.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.entity.Product;
import org.example.entity.ProductRelation;
import org.example.entity.User;
import org.example.repo.filter.FilterParam;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductRelationSpecification {
    public static <T extends ProductRelation> Specification<T> filterByParams(FilterParam filterParam, Object owner) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (owner != null) {
                if(owner instanceof User) {
                    predicates.add(cb.equal(root.get("user"), owner));
                }
                else {
                    predicates.add(cb.equal(root.get("family"), owner));
                }
            }


            if (filterParam.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filterParam.getStatus()));
            }

            if (filterParam.getFilterText() != null && !filterParam.getFilterText().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("product").get("name")), "%" + filterParam.getFilterText().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


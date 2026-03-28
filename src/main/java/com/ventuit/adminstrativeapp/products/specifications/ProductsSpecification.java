package com.ventuit.adminstrativeapp.products.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.models.BranchesProductsModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.products.dto.ProductsSearchCriteria;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ProductsSpecification {

    public static Specification<ProductsModel> searchByCriteria(ProductsSearchCriteria criteria) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            Join<ProductsModel, BranchesProductsModel> bpJoin = root.join("branchesProducts", JoinType.LEFT);
            Join<BranchesProductsModel, BranchesModel> bJoin = bpJoin.join("branch", JoinType.LEFT);
            Join<BranchesModel, BusinessesModel> buJoin = bJoin.join("business", JoinType.LEFT);

            if (criteria.getBusinessId() != null) {
                predicates.add(cb.equal(buJoin.get("id"), criteria.getBusinessId()));
                if (criteria.getBranchId() != null) {
                    predicates.add(cb.equal(bJoin.get("id"), criteria.getBranchId()));
                }
            }

            if (criteria.getDeletionStatus() != null) {
                if (criteria.getDeletionStatus() == DeletionStatus.NOT_DELETED) {
                    predicates.add(cb.isNull(root.get("deletedAt")));
                    predicates.add(cb.isTrue(root.get("active")));
                    predicates.add(cb.isNull(bpJoin.get("deletedAt")));
                    predicates.add(cb.isNull(bJoin.get("deletedAt")));
                    predicates.add(cb.isTrue(bJoin.get("active")));
                    predicates.add(cb.isNull(buJoin.get("deletedAt")));
                    predicates.add(cb.isTrue(buJoin.get("active")));
                } else if (criteria.getDeletionStatus() == DeletionStatus.DELETED) {
                    predicates.add(cb.isNotNull(root.get("deletedAt")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

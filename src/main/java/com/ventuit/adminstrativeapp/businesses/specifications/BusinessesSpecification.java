package com.ventuit.adminstrativeapp.businesses.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.models.BranchesProductsModel;
import com.ventuit.adminstrativeapp.businesses.dto.BusinessesSearchCriteria;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.shared.enums.DeletionStatus;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class BusinessesSpecification {
    public static Specification<BusinessesModel> searchByCriteria(BusinessesSearchCriteria criteria) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }

            if (criteria.getDeletionStatus() != null) {
                if (criteria.getDeletionStatus() == DeletionStatus.NOT_DELETED) {
                    predicates.add(cb.isNull(root.get("deletedAt")));
                    predicates.add(cb.isTrue(root.get("active")));

                    Join<BusinessesModel, BranchesModel> branchJoin = root.join("branches", JoinType.INNER);
                    predicates.add(cb.isNull(branchJoin.get("deletedAt")));
                    predicates.add(cb.isTrue(branchJoin.get("active")));

                    Join<BranchesModel, BranchesProductsModel> bpJoin = branchJoin.join("branchesProducts", JoinType.INNER);
                    predicates.add(cb.isNull(bpJoin.get("deletedAt")));

                    Join<BranchesProductsModel, ProductsModel> productJoin = bpJoin.join("product", JoinType.INNER);
                    predicates.add(cb.isNull(productJoin.get("deletedAt")));
                    predicates.add(cb.isTrue(productJoin.get("active")));
                } else if (criteria.getDeletionStatus() == DeletionStatus.DELETED) {
                    predicates.add(cb.isNotNull(root.get("deletedAt")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

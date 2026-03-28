package com.ventuit.adminstrativeapp.branches.repositories;

import org.springframework.stereotype.Repository;

import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.core.repositories.BaseRepository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BranchesRepository extends BaseRepository<BranchesModel, Integer> {

    long countByIdInAndDeletedAtIsNull(Collection<Integer> ids);

    default boolean existsAllByIdIn(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return countByIdInAndDeletedAtIsNull(ids) == ids.stream().distinct().count();
    }
}
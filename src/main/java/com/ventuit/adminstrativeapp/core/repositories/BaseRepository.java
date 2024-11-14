package com.ventuit.adminstrativeapp.core.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.ventuit.adminstrativeapp.core.models.BaseModel;

@NoRepositoryBean // Indicates that this is not a concrete repository
public interface BaseRepository<ENTITY extends BaseModel, ID> extends JpaRepository<ENTITY, ID> {

    @Modifying
    @Transactional
    default void softDeleteById(ID id) {
        findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            save(entity);
        });
    }

    @Modifying
    @Transactional
    default void softDelete(ENTITY entity) {
        entity.setDeletedAt(LocalDateTime.now());
        save(entity); // Save the entity to persist the soft deletion
    }

    @Modifying
    @Transactional
    default void restoreById(ID id) {
        findByIdAndDeletedAtIsNotNull(id).ifPresent(entity -> {
            entity.setDeletedAt(null);
            save(entity);
        });
    }

    @Modifying
    @Transactional
    default void restore(ENTITY entity) {
        entity.setDeletedAt(null);
        save(entity);
    }

    List<ENTITY> findByDeletedAtIsNull();

    Optional<ENTITY> findByIdAndDeletedAtIsNull(ID id);

    List<ENTITY> findByDeletedAtIsNotNull();

    Optional<ENTITY> findByIdAndDeletedAtIsNotNull(ID id);
}

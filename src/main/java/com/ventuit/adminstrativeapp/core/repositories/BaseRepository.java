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
    default void softRemoveById(ID id) {
        findById(id).ifPresent(entity -> {
            entity.setDeletedAt(LocalDateTime.now());
            save(entity);
        });
    }

    List<ENTITY> findByDeletedAtIsNull();

    Optional<ENTITY> findByIdAndDeletedAtIsNull(ID id);
}

package com.ventuit.adminstrativeapp.core.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@Data
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false, nullable = true)
    private LocalDateTime updatedAt;

    @Column(insertable = false, nullable = true)
    private LocalDateTime deletedAt;

    @PrePersist
    /**
     * This method is annotated with @PrePersist, indicating that it will be
     * executed before the entity is persisted (before it is inserted into the
     * database for the first time).
     */
    protected void onCreate() {
        // Set the 'createdAt' field to the current date and time
        this.createdAt = LocalDateTime.now();
    }
}

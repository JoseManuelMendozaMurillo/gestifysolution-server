package com.ventuit.adminstrativeapp.shared.models;

import jakarta.persistence.*;
import java.util.List;
import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "storage_providers")
public class StorageProvidersModel extends ExtendedBaseModel {
    @Column(nullable = false, unique = true)
    private String provider;

    @OneToMany(mappedBy = "storageProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BucketsModel> buckets;
}
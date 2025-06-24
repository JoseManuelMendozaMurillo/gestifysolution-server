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
@Table(name = "buckets")
public class BucketsModel extends ExtendedBaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String url;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FilesBucketsModel> filesBuckets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_provider_id", nullable = false)
    private StorageProvidersModel storageProvider;
}
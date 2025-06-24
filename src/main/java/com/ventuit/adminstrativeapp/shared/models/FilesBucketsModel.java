package com.ventuit.adminstrativeapp.shared.models;

import jakarta.persistence.*;

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
@Table(name = "files_buckets")
public class FilesBucketsModel extends ExtendedBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private FilesModel file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", nullable = false)
    private BucketsModel bucket;

    @Column(name = "bucket_order", nullable = false)
    private Integer bucketOrder;
}
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
@Table(name = "files")
public class FilesModel extends ExtendedBaseModel {

    @Column(name = "file_key", nullable = false, unique = true)
    private String fileKey;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private Double size;

    @Column(name = "blur_hash", nullable = true)
    private String blurHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "files_paths_id", nullable = true)
    private FilesPathsModel filesPaths;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FilesBucketsModel> filesBuckets;
}
package com.ventuit.adminstrativeapp.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import java.util.List;

import com.ventuit.adminstrativeapp.core.models.ExtendedBaseModel;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "files_paths")
public class FilesPathsModel extends ExtendedBaseModel {
    @Column(nullable = false, unique = true)
    private String path;

    @OneToMany(mappedBy = "filesPaths", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FilesModel> files;
}
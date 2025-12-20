package com.ventuit.adminstrativeapp.storage.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadataResponse {

    private String fileName;

    private String contentType;

    private long size;

    private ZonedDateTime lastModified;

    private String etag;

    private boolean isImage;

}

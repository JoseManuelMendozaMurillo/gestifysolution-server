package com.ventuit.adminstrativeapp.storage.dto;

import java.io.InputStream;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileProxyResponse {

    private InputStream inputStream;

    private String contentType;

    private long contentLength;

    private ZonedDateTime lastModified;

    private String etag;

}

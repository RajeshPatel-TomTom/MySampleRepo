/*
 * Copyright (C), the copyright owner 'TomTom', 2022.
 */
package com.tomtom;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

/**
 * @author srivapra
 */
@Builder(setterPrefix = "with", toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PBFMetadata {

    private Long deliveryID;

    private Violation violations;

    private ConflationRule conflationRule;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NonNull
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedTime;

    private SourceType sourceType;

    private String country;

    private String seroneVersion;

    private String ttomVersion;
}

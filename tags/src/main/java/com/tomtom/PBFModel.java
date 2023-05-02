/*
 * Copyright (C), the copyright owner 'TomTom', 2022.
 */
package com.tomtom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author srivapra
 */
@Builder(setterPrefix = "with", toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conflated_countries")
public class PBFModel implements Comparable<PBFModel> {

    @Id
    private String id;

    @NonNull
    private Long baseMapID;

    private Integer type;

    private Object tags;

    private String highway;

    private PBFMetadata metadata;

    private List<Object> nodes;
    private List<Object> relations;
    private PBFInfo info;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreatedDate
    private Date createdTimeStamp;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @LastModifiedDate
    private Date updatedTimeStamp;

    @JsonProperty(value = "isDeleted")
    @Builder.Default
    private boolean isDeleted = false;

    @Override
    public int compareTo(PBFModel o) {
        return this.baseMapID.compareTo(o.baseMapID);
    }
}

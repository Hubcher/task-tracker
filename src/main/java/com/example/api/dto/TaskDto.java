package com.example.api.dto;


import com.example.database.entity.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;
    private String name;
    private String description;
    private String teg;
    private Priority priority;

    @JsonProperty("created_at")
    private Instant created;
}

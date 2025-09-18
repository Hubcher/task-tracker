package com.example.api.dto;


import com.example.database.entity.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Builder
@Value
public class TaskReadDto {

    Long id;
    String name;
    String description;
    String teg;
    Priority priority;
    @JsonProperty("created_at")
    Instant created;
}

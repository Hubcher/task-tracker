package com.example.api.dto;



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
public class TaskStateDto {
    private Long id;
    private String name;

    @JsonProperty("created_at")
    private Instant createdAt;

}

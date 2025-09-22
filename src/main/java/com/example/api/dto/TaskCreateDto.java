package com.example.api.dto;

import com.example.database.entity.Priority;
import lombok.*;

@Builder
@Value
public class TaskCreateDto {
    String name;
    String description;
    String teg;
    Priority priority;

}

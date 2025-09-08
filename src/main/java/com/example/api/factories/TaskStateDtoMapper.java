package com.example.api.factories;

import com.example.api.dto.TaskStateDto;
import com.example.database.entity.TaskStateModel;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoMapper {

    public TaskStateDto makeTaskStateDto(TaskStateModel entity) {

        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

package com.example.api.factories;

import com.example.api.dto.TaskDto;
import com.example.database.entity.TaskModel;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper {

    public TaskDto makeTaskDto(TaskModel entity) {
        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .teg(entity.getTeg())
                .description(entity.getDescription())
                .priority(entity.getPriority())
                .created(entity.getCreated())
                .build();

    }

}

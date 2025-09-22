package com.example.api.factories;

import com.example.api.dto.TaskCreateDto;
import com.example.api.dto.TaskReadDto;
import com.example.api.dto.TaskUpdateDto;
import com.example.database.entity.TaskModel;
import com.example.database.entity.TaskStateModel;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper {

    // entity -> read dto
    public TaskReadDto makeTaskDto(TaskModel entity) {
        return TaskReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .teg(entity.getTeg())
                .priority(entity.getPriority())
                .created(entity.getCreated())
                .build();
    }

    // create dto -> entity
    public TaskModel makeTaskEntity(TaskCreateDto dto, TaskStateModel taskState) {
        return TaskModel.builder()
                .taskState(taskState)
                .name(dto.getName())
                .description(dto.getDescription())
                .teg(dto.getTeg())
                .priority(dto.getPriority())
                .build();
    }

    // update dto -> update entity
    public void updateTaskEntity(TaskUpdateDto dto, TaskModel entity) {
        if (dto.getName() != null && !dto.getName().isBlank()) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getTeg() != null) {
            entity.setTeg(dto.getTeg());
        }
        if (dto.getPriority() != null) {
            entity.setPriority(dto.getPriority());
        }
    }

}

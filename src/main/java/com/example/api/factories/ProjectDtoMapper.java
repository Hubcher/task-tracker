package com.example.api.factories;

import com.example.api.dto.ProjectDto;
import com.example.database.entity.ProjectModel;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoMapper {

    public ProjectDto makeProjectDto(ProjectModel entity) {

        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}

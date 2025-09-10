package com.example.service;


import com.example.api.dto.AckDto;
import com.example.api.dto.ProjectDto;
import com.example.api.exceptions.BadRequestException;
import com.example.api.exceptions.NotFoundException;
import com.example.api.factories.ProjectDtoMapper;
import com.example.database.entity.ProjectModel;
import com.example.database.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectDtoMapper projectDtoMapper;

    public List<ProjectDto> fetchProjects(Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName
                .filter(prefixName -> !prefixName.trim().isEmpty());

        List<ProjectModel> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream.stream().map(projectDtoMapper::makeProjectDto)
                .collect(Collectors.toList());
    }

    public ProjectDto createOrUpdateProject(Optional<Long> optionalProjectId, Optional<String> optionalProjectName) {

        /*
         * create: /api/projects?project_name=
         * update: /api/projects/52?project_name=
         * */

        final ProjectModel project;

        optionalProjectName = optionalProjectName
                .filter(prefixName -> !prefixName.trim().isEmpty());

        boolean isCreate = optionalProjectId.isEmpty();

        if (isCreate && optionalProjectName.isEmpty()) {
            throw new BadRequestException("Project name cannot be empty.");
        }

        if (optionalProjectId.isPresent()) {
            Long projectId = optionalProjectId.get();
            project = projectRepository.findById(optionalProjectId.get())
                    .orElseThrow(() -> new NotFoundException(
                            "Project with ID %d not found."
                                    .formatted(projectId)));
        } else {
            project = ProjectModel.builder().build();
        }

        optionalProjectName
                .ifPresent(projectName -> {
                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException("Project %s already exists".formatted(projectName));
                            });
                    project.setName(projectName);
                });

        final ProjectModel savedProject = projectRepository.saveAndFlush(project);

        return projectDtoMapper.makeProjectDto(project);
    }

    public AckDto deleteProject(Long projectId) {
        projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Project with ID %s not found".formatted(projectId)
                        ));

        projectRepository.deleteById(projectId);
        return AckDto.getAnswer(true);
    }

}

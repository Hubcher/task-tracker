package com.example.integration.service;

import com.example.annotation.IT;
import com.example.api.dto.ProjectDto;
import com.example.api.exceptions.BadRequestException;
import com.example.api.exceptions.NotFoundException;
import com.example.database.entity.ProjectModel;
import com.example.database.repository.ProjectRepository;
import com.example.service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IT
public class ProjectServiceIT {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("Список всех проектов")
    void findAllProjects() {
        projectRepository.saveAndFlush(ProjectModel.builder().name("Database").build());
        projectRepository.saveAndFlush(ProjectModel.builder().name("Auth").build());

        List<ProjectDto> result = projectService.fetchProjects(Optional.empty());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Поиск по префиксу")
    void findProjectsWithPrefix_returnsFiltered() {

        projectRepository.saveAndFlush(ProjectModel.builder().name("Database").build());
        projectRepository.saveAndFlush(ProjectModel.builder().name("Auth").build());

        List<ProjectDto> result = projectService.fetchProjects(Optional.of("Da"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Database");
    }

    @Test
    @DisplayName("Создание проекта")
    void createProject_success() {
        ProjectDto result = projectService.createOrUpdateProject(
                Optional.empty(),
                Optional.of("Analytics")
        );

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Analytics");
    }

    @Test
    @DisplayName("Создание с пустым именем")
    void createProject_throwsIfNameEmpty() {
        assertThatThrownBy(() -> projectService.createOrUpdateProject(Optional.empty(), Optional.empty()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Project name cannot be empty");
    }

    @Test
    @DisplayName("Обновление имени проекта")
    void updateProject_success() {
        ProjectModel saved = projectRepository.saveAndFlush(ProjectModel.builder().name("OldName").build());

        ProjectDto updated = projectService.createOrUpdateProject(
                Optional.of(Long.valueOf(saved.getId())),
                Optional.of("NewName")
        );

        assertThat(updated.getName()).isEqualTo("NewName");
    }

    @Test
    @DisplayName("Обновление несуществующего проекта")
    void updateProject_throwsIfProjectNotFound() {
        assertThatThrownBy(() -> projectService.createOrUpdateProject(Optional.of(999L), Optional.of("X")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Project with ID 999 not found");
    }

    @Test
    @DisplayName("Попытка обновления на уже существующее имя")
    void updateProject_throwsIfNameAlreadyExists() {
        projectRepository.saveAndFlush(ProjectModel.builder().name("ExistingName").build());

        ProjectModel project = projectRepository.saveAndFlush(ProjectModel.builder().name("Target").build());

        assertThatThrownBy(() -> projectService.createOrUpdateProject(
                Optional.of(Long.valueOf(project.getId())), Optional.of("ExistingName")))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Удаление проекта")
    void deleteProject_success() {
        ProjectModel saved = projectRepository.saveAndFlush(ProjectModel.builder().name("toDelete").build());

        projectService.deleteProject(Long.valueOf(saved.getId()));

        assertThat(projectRepository.findById(Long.valueOf(saved.getId()))).isEmpty();
    }


    @Test
    @DisplayName("Удаление несуществующего проекта")
    void deleteProject_failed() {
        assertThatThrownBy(() -> projectService.deleteProject(-123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Project with id \"%d\" does not exist".formatted(-123L));
    }



}

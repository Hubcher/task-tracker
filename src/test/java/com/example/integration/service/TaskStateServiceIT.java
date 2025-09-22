package com.example.integration.service;

import com.example.annotation.IT;
import com.example.api.dto.AckDto;
import com.example.api.dto.TaskStateDto;
import com.example.api.exceptions.BadRequestException;
import com.example.database.entity.ProjectModel;
import com.example.database.entity.TaskStateModel;
import com.example.database.repository.ProjectRepository;
import com.example.database.repository.TaskStateRepository;
import com.example.service.TaskStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IT
public class TaskStateServiceIT {

    @Autowired
    private TaskStateService taskStateService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStateRepository taskStateRepository;

    private ProjectModel project;

    @BeforeEach
    void setUp() {
        taskStateRepository.deleteAll();
        projectRepository.deleteAll();

        project = projectRepository.saveAndFlush(
                ProjectModel.builder()
                        .name("Test Project")
                        .build()
        );
    }

    @Test
    @DisplayName("Создание нового состояния задачи")
    void createTaskState_success() {
        TaskStateDto state = taskStateService.createTaskState(Long.valueOf(project.getId()), "in progress");

        assertThat(state).isNotNull();
        assertThat(state.getName()).isEqualTo("in progress");

        List<TaskStateModel> allStates = taskStateRepository.findAll();
        assertThat(allStates).hasSize(1);
    }

    @Test
    @DisplayName("Создание состояния с пустым именем -> BadRequestException")
    void createTaskState_emptyName() {
        assertThatThrownBy(() -> taskStateService.createTaskState(Long.valueOf(project.getId()), ""))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("can't be empty");
    }

    @Test
    @DisplayName("Создание дубликата состояния")
    void createTaskState_duplicate() {

        taskStateService.createTaskState(Long.valueOf(project.getId()), "duplicateName");

        assertThatThrownBy(() -> taskStateService.createTaskState(Long.valueOf(project.getId()), "duplicateName"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exist");
    }

    @Test
    @DisplayName("Получение списка состояний у проекта")
    void getTaskStates() {
        taskStateService.createTaskState(Long.valueOf(project.getId()), "first task state of project");
        taskStateService.createTaskState(Long.valueOf(project.getId()), "second task state of project");

        List<TaskStateDto> taskStates = taskStateService.getTaskStates(Long.valueOf(project.getId()));

        assertThat(taskStates).hasSize(2);
        assertThat(taskStates).extracting(TaskStateDto::getName)
                .containsExactlyInAnyOrder("first task state of project", "second task state of project");
    }

    @Test
    @DisplayName("Удаление состояния задачи у проекта")
    void deleteTaskState() {
        TaskStateDto state = taskStateService.createTaskState(Long.valueOf(project.getId()), "toDeleteTaskState");

        AckDto ackDto = taskStateService.deleteTaskState(Long.valueOf(project.getId()), state.getId());

        assertThat(ackDto.isAnswer()).isTrue();
        assertThat(taskStateRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Обновление состояния у проекта")
    void updateTaskState_success() {

        TaskStateDto state = taskStateService.createTaskState(Long.valueOf(project.getId()), "toUpdateTaskState");

        TaskStateDto updateTaskState = taskStateService.updateTaskState(
                Long.valueOf(project.getId()), state.getId(), "updateTaskState");

        assertThat(updateTaskState.getName()).isEqualTo("updateTaskState");

    }

    @Test
    @DisplayName("Обновление состояния с пустым именем → BadRequestException")
    void updateTaskState_emptyName() {
        TaskStateDto state = taskStateService.createTaskState(Long.valueOf(project.getId()), "reviewName");

        assertThatThrownBy(() ->
                taskStateService.updateTaskState(Long.valueOf(project.getId()), state.getId(), ""))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("can't be empty");
    }

    @Test
    @DisplayName("Обновление состояния на дубликат → BadRequestException")
    void updateTaskState_duplicate() {

        TaskStateDto first = taskStateService.createTaskState(Long.valueOf(project.getId()), "first");
        taskStateService.createTaskState(Long.valueOf(project.getId()), "second");

        assertThatThrownBy(() ->
                taskStateService.updateTaskState(Long.valueOf(project.getId()), first.getId(), "second"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");
    }




}

package com.example.integration.service;

import com.example.annotation.IT;
import com.example.api.dto.AckDto;
import com.example.api.dto.TaskCreateDto;
import com.example.api.dto.TaskReadDto;
import com.example.api.dto.TaskUpdateDto;
import com.example.api.exceptions.BadRequestException;
import com.example.database.entity.Priority;
import com.example.database.entity.ProjectModel;
import com.example.database.entity.TaskStateModel;
import com.example.database.repository.ProjectRepository;
import com.example.database.repository.TaskRepository;
import com.example.database.repository.TaskStateRepository;
import com.example.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IT
public class TaskServiceIT {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStateRepository taskStateRepository;

    @Autowired
    private TaskRepository taskRepository;

    private ProjectModel project;
    private TaskStateModel taskState;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskStateRepository.deleteAll();
        projectRepository.deleteAll();

        project = projectRepository.saveAndFlush(ProjectModel.builder()
                .name("Test Project")
                .build());

        taskState = taskStateRepository.saveAndFlush(TaskStateModel.builder()
                .name("Initial TaskState")
                .project(project)
                .build());
    }

    @Test
    @DisplayName("Создание новой задачи")
    void createTask_success() {
        TaskCreateDto dto = TaskCreateDto.builder()
                .name("New Task")
                .description("Description")
                .teg("backend")
                .priority(Priority.HIGH)
                .build();

        TaskReadDto task = taskService.createTask(Long.valueOf(project.getId()), taskState.getId(), dto);

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo("New Task");
        assertThat(taskRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Создание задачи с пустым именем -> BadRequestException")
    void createTask_emptyName() {
        TaskCreateDto dto = TaskCreateDto.builder()
                .name(" ")
                .build();

        assertThatThrownBy(() -> taskService.createTask(Long.valueOf(project.getId()), taskState.getId(), dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("can't be empty");
    }

    @Test
    @DisplayName("Создание дубликата задачи -> BadRequestException")
    void createTask_duplicate() {
        TaskCreateDto dto = TaskCreateDto.builder()
                .name("Duplicate Task")
                .build();

        taskService.createTask(Long.valueOf(project.getId()), taskState.getId(), dto);

        assertThatThrownBy(() -> taskService.createTask(Long.valueOf(project.getId()), taskState.getId(), dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Получение списка задач")
    void getTasks_success() {
        taskService.createTask(Long.valueOf(project.getId()), taskState.getId(),
                TaskCreateDto.builder().name("Task1").build());
        taskService.createTask(Long.valueOf(project.getId()), taskState.getId(),
                TaskCreateDto.builder().name("Task2").build());

        List<TaskReadDto> tasks = taskService.getTasks(Long.valueOf(project.getId()), taskState.getId());

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(TaskReadDto::getName)
                .containsExactlyInAnyOrder("Task1", "Task2");
    }

    @Test
    @DisplayName("Удаление задачи")
    void deleteTask_success() {
        TaskReadDto task = taskService.createTask(Long.valueOf(project.getId()), taskState.getId(),
                TaskCreateDto.builder().name("ToDelete").build());

        AckDto ack = taskService.deleteTask(Long.valueOf(project.getId()), taskState.getId(), task.getId());

        assertThat(ack.isAnswer()).isTrue();
        assertThat(taskRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Обновление задачи")
    void updateTask_success() {
        TaskReadDto task = taskService.createTask(Long.valueOf(project.getId()), taskState.getId(),
                TaskCreateDto.builder()
                        .name("Old Name")
                        .description("Old desc")
                        .teg("oldTeg")
                        .priority(Priority.LOW)
                        .build()
        );

        TaskUpdateDto updateDto = TaskUpdateDto.builder()
                .name("Updated Name")
                .description("Updated desc")
                .teg("newTeg")
                .priority(Priority.HIGH)
                .build();

        TaskReadDto updated = taskService.updateTask(Long.valueOf(project.getId()), taskState.getId(), task.getId(), updateDto);

        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getDescription()).isEqualTo("Updated desc");
        assertThat(updated.getTeg()).isEqualTo("newTeg");
        assertThat(updated.getPriority()).isEqualTo(Priority.HIGH);
    }

}

package com.example.aop;

import com.example.api.exceptions.NotFoundException;
import com.example.database.repository.ProjectRepository;
import com.example.database.repository.TaskStateRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectExistenceAspect {

    private final ProjectRepository projectRepository;
    private final TaskStateRepository taskStateRepository;

    // TODO возможно понадобятся именно поинткаты в дальнейшем
    @Before("@annotation(com.example.aop.CheckProjectExists) && args(projectId,..)")
    public void validateProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project with id \"%d\" does not exist.".formatted(projectId));
        }
    }

    @Before(value = "@annotation(com.example.aop.CheckTaskStateExists) && args(projectId, taskStateId, ..)", argNames = "projectId,taskStateId")
    public void validateTaskStateExists(Long projectId, Long taskStateId) {
        if (!taskStateRepository.existsById(taskStateId)) {
            throw new NotFoundException("TaskState \"%d\" not found in Project \"%d\"".formatted(taskStateId, projectId));
        }
    }


}
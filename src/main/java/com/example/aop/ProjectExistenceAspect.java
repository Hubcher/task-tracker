package com.example.aop;

import com.example.api.exceptions.NotFoundException;
import com.example.database.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectExistenceAspect {

    private final ProjectRepository projectRepository;
    @Before("@annotation(com.example.aop.CheckProjectExists) && args(projectId,..)")
    public void validateProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new NotFoundException("Project with id %d does not exist.".formatted(projectId));
        }
    }

}

package com.example.database.repository;

import com.example.database.entity.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

    Optional<ProjectModel> findByName(String name);

    List<ProjectModel> streamAllBy();

    List<ProjectModel> streamAllByNameStartsWithIgnoreCase(String prefixName);

}

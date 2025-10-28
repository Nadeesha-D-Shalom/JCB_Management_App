package com.example.JCB_mgnmtApp.repository;

import com.example.JCB_mgnmtApp.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

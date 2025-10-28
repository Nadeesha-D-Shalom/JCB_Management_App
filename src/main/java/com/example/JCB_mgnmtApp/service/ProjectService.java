package com.example.JCB_mgnmtApp.service;

import com.example.JCB_mgnmtApp.model.Project;
import com.example.JCB_mgnmtApp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) {
        this.repo = repo;
    }

    public List<Project> listAll() { return repo.findAll(); }
    public Project getById(Long id) { return repo.findById(id).orElseThrow(); }

    public Project save(Project project) { return repo.save(project); }

    public void deleteById(Long id) { repo.deleteById(id); }
}

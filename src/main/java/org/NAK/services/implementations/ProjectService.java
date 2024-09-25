package org.NAK.services.implementations;

import org.NAK.entities.Project;
import org.NAK.reposetories.implementations.ProjectRepository;
import org.NAK.services.contracts.ProjectInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectService implements ProjectInterface {

    private final ProjectRepository projectRepository;

    public ProjectService() {
        this.projectRepository = new ProjectRepository();
    }

    public void addProject(Project project) {
        if (isValidProject(project)) {
            try {
                projectRepository.addProject(project);
                System.out.println("Project added successfully: " + project.getProjectName());
            } catch (SQLException e) {
                System.err.println("Error adding project: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid project data: " + project);
        }
    }

    public Optional<Project> findProjectById(UUID projectId) {
        try {
            return projectRepository.findProjectById(projectId);
        } catch (SQLException e) {
            System.err.println("Error finding project by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Project> getAllProjects() {
        try {
            return projectRepository.getAllProjects();
        } catch (SQLException e) {
            System.err.println("Error retrieving projects: " + e.getMessage());
            return List.of();
        }
    }


    private boolean isValidProject(Project project) {
        if (project == null) {
            return false;
        }
        if (project.getProjectName() == null || project.getProjectName().isEmpty()) {
            return false;
        }
        if (project.getProfitMargin() < 0) {
            return false;
        }
        if (project.getClient() == null || project.getClient().getId() == null) {
            return false;
        }
        return true;
    }
}

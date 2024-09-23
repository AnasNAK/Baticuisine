package org.NAK.reposetories.contracts;

import org.NAK.entities.Project;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ProjectInterface {

    void addProject(Project project) throws SQLException;
    Optional<Project> findProjectById(UUID projectId) throws SQLException;
    List<Project> getAllProjects() throws SQLException;
    Optional<Project> updateProject(UUID projectId, Project updatedProject) throws SQLException;
    Optional<Project> deleteProject(UUID projectId) throws SQLException;
}
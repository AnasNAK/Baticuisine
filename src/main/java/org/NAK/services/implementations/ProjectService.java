package org.NAK.services.implementations;

import org.NAK.entities.Material;
import org.NAK.entities.Project;
import org.NAK.entities.WorkForce;
import org.NAK.reposetories.implementations.MaterialRepository;
import org.NAK.reposetories.implementations.ProjectRepository;
import org.NAK.reposetories.implementations.WorkForceRepository;
import org.NAK.services.contracts.ProjectInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectService implements ProjectInterface {

    private final ProjectRepository projectRepository;
    private final MaterialService materialService;
    private final WorkForceService workForceService;

    public ProjectService() {
        this.projectRepository = new ProjectRepository();
        this.materialService = new MaterialService();
        this.workForceService = new WorkForceService();
    }

    public Project addProject(Project project) {
        if (isValidProject(project)) {
            try {

                Double totalMaterials = materialService.calculateTotalMaterials(project.getMaterials());
                Double totalWorkForces = workForceService.calculateTotalWorkForce(project.getWorkForces());

                Double marge = (totalMaterials + totalWorkForces) * project.getProfitMargin() / 100;

                Double PriceWithoutMarge = totalMaterials + totalWorkForces;

                project.setTotalCost(marge + PriceWithoutMarge);

                projectRepository.addProject(project);

                project.getMaterials().forEach(e -> {
                    e.setProject(project);
                    try {
                        materialService.addMaterial(e);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                project.getWorkForces().forEach(e ->{
                    e.setProject(project);
                    try {
                        workForceService.addWorkForce(e);
                    }catch (SQLException ex){
                        throw new RuntimeException(ex);

                    }
                });

                System.out.println("Project added successfully: " + project.getProjectName());
            } catch (SQLException e) {
                System.err.println("Error adding project: " + e.getMessage());
            }
        } else {
            System.err.println("Invalid project data: " + project);
        }
        return project;
    }

    public Optional<Project> findProjectById(UUID projectId) throws SQLException {
        Optional<Project> project = projectRepository.findProjectById(projectId);

        List<Material> materialList = materialService.findMaterialById(projectId);
        List<WorkForce> workForceList = workForceService.findWorkForceById(projectId);

        project.get().setWorkForces(workForceList);
        project.get().setMaterials(materialList);

        return project;
    }

    public List<Project> getAllProjects() throws SQLException {

        List<Project> projectList = projectRepository.getAllProjects();
        projectList.stream()
        .forEach(project -> {
            try {
                List<Material> materialList = materialService.findMaterialById(project.getId());
                project.setMaterials(materialList);
                List<WorkForce> workForceList = workForceService.findWorkForceById(project.getId());
                project.setWorkForces(workForceList);

            }catch (SQLException e){
                throw new RuntimeException(e);
            }

        });
        return projectList;
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

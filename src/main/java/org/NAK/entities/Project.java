package org.NAK.entities;

import org.NAK.enums.ProjectState;

import java.util.List;
import java.util.UUID;

public class Project {

    private UUID id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    private ProjectState projectState;
    private Client client;
    private List<WorkForce> workForces;
    private List<Material> materials;
    private List<Estimate> estimates;

    public Project(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public void setProjectState(ProjectState projectState) {
        this.projectState = projectState;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<WorkForce> getWorkForces() {
        return workForces;
    }

    public void setWorkForces(List<WorkForce> workForces) {
        this.workForces = workForces;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<Estimate> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<Estimate> estimates) {
        this.estimates = estimates;
    }
}

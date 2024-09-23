package org.NAK.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Estimate {

    private UUID id;
    private double EstimatedAmount;
    private LocalDate EstimatedDate;
    private LocalDate ValidityDate;
    private boolean accepted;
    private Project project;

    public Estimate(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getEstimatedAmount() {
        return EstimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        EstimatedAmount = estimatedAmount;
    }

    public LocalDate getEstimatedDate() {
        return EstimatedDate;
    }

    public void setEstimatedDate(LocalDate estimatedDate) {
        EstimatedDate = estimatedDate;
    }

    public LocalDate getValidityDate() {
        return ValidityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        ValidityDate = validityDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}

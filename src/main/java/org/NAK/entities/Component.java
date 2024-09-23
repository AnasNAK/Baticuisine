package org.NAK.entities;

import org.NAK.enums.ComponentType;

import java.util.List;
import java.util.UUID;

public class Component {

    private UUID id;
    private String name;
    private ComponentType ComponentType;
    private double TvaRate;
    private double UnitaryCost;
    private double quantity;
    private double OutputFactor;
    private Project project;

    public Component(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentType getComponentType() {
        return ComponentType;
    }

    public void setComponentType(ComponentType componentType) {
        ComponentType = componentType;
    }

    public double getTvaRate() {
        return TvaRate;
    }

    public void setTvaRate(double tvaRate) {
        TvaRate = tvaRate;
    }

    public double getUnitaryCost() {
        return UnitaryCost;
    }

    public void setUnitaryCost(double unitaryCost) {
        UnitaryCost = unitaryCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getOutputFactor() {
        return OutputFactor;
    }

    public void setOutputFactor(double outputFactor) {
        OutputFactor = outputFactor;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
package org.NAK.services.implementations;

import org.NAK.entities.WorkForce;
import org.NAK.reposetories.implementations.WorkForceRepository;
import org.NAK.services.contracts.WorkForceInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkForceService implements WorkForceInterface {

    private WorkForceRepository workForceRepository;

    public WorkForceService() {
        this.workForceRepository = new WorkForceRepository();
    }

    @Override
    public void addWorkForce(WorkForce workForce) throws SQLException {
        validateWorkForce(workForce);
        workForceRepository.addWorkForce(workForce);
    }

    @Override
    public List<WorkForce> findWorkForceById(UUID projectid) throws SQLException {
        return workForceRepository.findWorkForceById(projectid);
    }

    @Override
    public double calculateTotalWorkForce(List<WorkForce> workForceList) {

        Double total = null;
        if (!workForceList.isEmpty()){
            Double totalSansTVA = workForceList.stream()
                    .map(workForce -> {
                        double quantityXUniCost = workForce.getQuantity() * workForce.getUnitaryCost();
                        return quantityXUniCost * workForce.getOutputFactor();
                    })
                    .reduce(0.0,Double::sum);
            Double tva = (totalSansTVA * workForceList.getFirst().getTvaRate()) / 100;
            total = totalSansTVA +tva;
        }
        return total;

    }

    private void validateWorkForce(WorkForce workForce) {
        if (workForce.getName() == null || workForce.getName().isEmpty()) {
            throw new IllegalArgumentException("Workforce name cannot be null or empty.");
        }
        if (workForce.getComponentType() == null) {
            throw new IllegalArgumentException("Component type cannot be null.");
        }
        if (workForce.getUnitaryCost() < 0) {
            throw new IllegalArgumentException("Unitary cost cannot be negative.");
        }
        if (workForce.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (workForce.getOutputFactor() <= 0) {
            throw new IllegalArgumentException("Output factor must be greater than zero.");
        }
    }
}

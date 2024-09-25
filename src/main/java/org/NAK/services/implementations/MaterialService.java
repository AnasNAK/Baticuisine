package org.NAK.services.implementations;

import org.NAK.entities.Material;
import org.NAK.reposetories.implementations.MaterialRepository;
import org.NAK.services.contracts.MaterialInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MaterialService implements MaterialInterface {

    private  MaterialRepository materialRepository;

    public MaterialService() {
        this.materialRepository = new MaterialRepository();
    }

    public void addMaterial(Material material) throws SQLException {
        validateMaterial(material);
        materialRepository.addMaterial(material);
    }

    public List<Material> findMaterialById(UUID projectid) throws SQLException {
        return materialRepository.findMaterialById(projectid);
    }

    public double calculateTotalMaterials(List<Material> materialList) {
        Double total = null;

        if (!materialList.isEmpty()) {
            Double totalSansTVA = materialList.stream()
                    .map(material -> {
                        double quantityXUnitCost = material.getQuantity() * material.getUnitaryCost();
                        return (quantityXUnitCost * material.getOutputFactor()) + material.getTransportCost();
                    })
                    .reduce(0.0, Double::sum);

            Double tva = (totalSansTVA * materialList.getFirst().getTvaRate()) / 100;

            total = totalSansTVA + tva;
        }

        return total;
    }

    private void validateMaterial(Material material) {
        if (material.getName() == null || material.getName().isEmpty()) {
            throw new IllegalArgumentException("Material name cannot be null or empty.");
        }
        if (material.getUnitaryCost() <= 0) {
            throw new IllegalArgumentException("Unitary cost must be greater than zero.");
        }
        if (material.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (material.getOutputFactor() <= 0) {
            throw new IllegalArgumentException("Output factor must be greater than zero.");
        }
    }
}

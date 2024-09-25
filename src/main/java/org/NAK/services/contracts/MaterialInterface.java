package org.NAK.services.contracts;

import org.NAK.entities.Material;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialInterface {

    void addMaterial(Material material) throws SQLException;
    Optional<Material> findMaterialById(UUID materialId) throws SQLException;
    double calculateTotalMaterials(List<Material> material);
}

package org.NAK.reposetories.contracts;

import org.NAK.entities.Material;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface MaterialInterface {

    void addMaterial(Material material) throws SQLException;
    Optional<Material> findMaterialById(UUID materialId) throws SQLException;
//    List<Material> getAllMaterials() throws SQLException;
//    Optional<Material> updateMaterial(UUID materialId, Material updatedMaterial) throws SQLException;
//    Optional<Material> deleteMaterial(UUID materialId) throws SQLException;
}
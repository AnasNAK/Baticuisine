package org.NAK.reposetories.implementations;

import org.NAK.db.Database;
import org.NAK.entities.Material;
import org.NAK.reposetories.contracts.MaterialInterface;
import org.NAK.enums.ComponentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MaterialRepository implements MaterialInterface {

    private final Connection connection;

    public MaterialRepository() {
        this.connection = Database.getInstance().establishConnection();
    }

    @Override
    public void addMaterial(Material material) throws SQLException {
        String sql = "INSERT INTO material ( name, componenttype, tvarate, unitarycost, quantity, outputfactor, transportcost, projectid) " +
                "VALUES ( ?, ?::componenttype, ?, ?, ?, ?, ?, ?::uuid)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, material.getName());
            stmt.setString(2, material.getComponentType().name());
            stmt.setDouble(3, material.getTvaRate());
            stmt.setDouble(4, material.getUnitaryCost());
            stmt.setDouble(5, material.getQuantity());
            stmt.setDouble(6, material.getOutputFactor());
            stmt.setDouble(7, material.getTransportCost());
            stmt.setObject(8, material.getProject().getId());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()){
                material.setId(UUID.fromString(generatedKeys.getString(1)));
            }
        }
    }

    @Override
    public List<Material> findMaterialById(UUID projectId) throws SQLException {
        String sql = "SELECT * FROM material WHERE projectid = ?";
        List<Material> materials = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Material material = new Material();
                    material.setId(UUID.fromString(rs.getString("id")));
                    material.setName(rs.getString("name"));
                    material.setComponentType(ComponentType.valueOf(rs.getString("componenttype")));
                    material.setTvaRate(rs.getDouble("tvarate"));
                    material.setUnitaryCost(rs.getDouble("unitarycost"));
                    material.setQuantity(rs.getDouble("quantity"));
                    material.setOutputFactor(rs.getDouble("outputfactor"));
                    material.setTransportCost(rs.getDouble("transportcost"));

                    materials.add(material);
                }
            }
        }

        return materials;
    }


}

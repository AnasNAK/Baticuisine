package org.NAK.reposetories.implementations;

import org.NAK.db.Database;
import org.NAK.entities.WorkForce;
import org.NAK.enums.ComponentType;
import org.NAK.reposetories.contracts.WorkForceInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkForceRepository implements WorkForceInterface {

    private final Connection connection;

    public WorkForceRepository() {
        this.connection = Database.getInstance().establishConnection();
    }

    @Override
    public void addWorkForce(WorkForce workForce) throws SQLException {
        String sql = "INSERT INTO workforce (name, componenttype, tvarate, unitarycost, quantity, outputfactor, projectid) " +
                "VALUES (?, ?::componenttype, ?, ?, ?, ?, ?::uuid)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, workForce.getName());
            stmt.setString(2, workForce.getComponentType().name());
            stmt.setDouble(3, workForce.getTvaRate());
            stmt.setDouble(4, workForce.getUnitaryCost());
            stmt.setDouble(5, workForce.getQuantity());
            stmt.setDouble(6, workForce.getOutputFactor());
            stmt.setObject(7, workForce.getProject().getId());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                workForce.setId(UUID.fromString(generatedKeys.getString(1)));
            }
        }
    }

    @Override
    public List<WorkForce> findWorkForceById(UUID projectId) throws SQLException {
        String query = "SELECT * FROM workforce WHERE projectid = ?";
        List<WorkForce> workForces = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WorkForce workForce = mapResultSetToWorkForce(rs);
                workForces.add(workForce);
            }
        }

        return workForces;
    }

    private WorkForce mapResultSetToWorkForce(ResultSet rs) throws SQLException {
        WorkForce workForce = new WorkForce();
        workForce.setId(UUID.fromString(rs.getString("id")));
        workForce.setName(rs.getString("name"));
        workForce.setComponentType(ComponentType.valueOf(rs.getString("componenttype")));
        workForce.setTvaRate(rs.getDouble("tvarate"));
        workForce.setUnitaryCost(rs.getDouble("unitarycost"));
        workForce.setQuantity(rs.getDouble("quantity"));
        workForce.setOutputFactor(rs.getDouble("outputfactor"));
        return workForce;
    }

}

package org.NAK.services.contracts;

import org.NAK.entities.WorkForce;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkForceInterface {

    void addWorkForce(WorkForce workForce) throws SQLException;
    List <WorkForce> findWorkForceById(UUID projectid) throws SQLException;
    double calculateTotalWorkForce(List<WorkForce> workForce);
}

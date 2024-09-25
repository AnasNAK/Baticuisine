package org.NAK.reposetories.contracts;

import org.NAK.entities.WorkForce;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface WorkForceInterface {

    void addWorkForce(WorkForce workForce) throws SQLException;
    List<WorkForce> findWorkForceById(UUID projectid) throws SQLException;
//    List<WorkForce> getAllWorkForces() throws SQLException;
//    Optional<WorkForce> updateWorkForce(UUID workForceId, WorkForce updatedWorkForce) throws SQLException;
//    Optional<WorkForce> deleteWorkForce(UUID workForceId) throws SQLException;
}

package org.NAK.reposetories.contracts;

import org.NAK.entities.Estimate;

import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface EstimateInterface {

    void addEstimate(Estimate estimate) throws SQLException;
    Optional<Estimate> findEstimateById(UUID estimateId) throws SQLException;
    List<Estimate> getAllEstimates() throws SQLException;
    Optional<Estimate> updateEstimate(UUID estimateId, Estimate updatedEstimate) throws SQLException;
    Optional<Estimate> deleteEstimate(UUID estimateId) throws SQLException;
}

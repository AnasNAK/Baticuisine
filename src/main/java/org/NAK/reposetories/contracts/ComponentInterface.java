package org.NAK.reposetories.contracts;

import org.NAK.entities.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComponentInterface {

    void addComponent(Component component) throws SQLException;
    Optional<Component> findComponentById(UUID componentId) throws SQLException;
    List<Component> getAllComponents() throws SQLException;
    Optional<Component> updateComponent(UUID componentId, Component updatedComponent) throws SQLException;
    Optional<Component> deleteComponent(UUID componentId) throws SQLException;

}

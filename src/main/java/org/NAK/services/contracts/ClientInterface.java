package org.NAK.services.contracts;

import org.NAK.entities.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientInterface {
    void addClient(Client client) throws SQLException;
    Optional<Client> findClientById(UUID clientId) throws SQLException;
    List<Client> getAllClients() throws SQLException;
    Optional<Client> updateClient(UUID clientId, Client updatedClient) throws SQLException;
    Optional<Client> deleteClient(UUID clientId) throws SQLException;
}

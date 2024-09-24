package org.NAK.services.implementations;

import org.NAK.entities.Client;
import org.NAK.reposetories.implementations.ClientRepository;
import org.NAK.services.contracts.ClientInterface;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientService implements ClientInterface {
    private ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    @Override
    public void addClient(Client client) throws SQLException {
        clientRepository.addClient(client);
    }

    @Override
    public Optional<Client> findClientById(UUID clientId) throws SQLException {
        return clientRepository.findClientById(clientId);
    }

    @Override
    public List<Client> getAllClients() throws SQLException {
        return clientRepository.getAllClients();
    }

    @Override
    public Optional<Client> updateClient(UUID clientId, Client updatedClient) throws SQLException {
        return clientRepository.updateClient(clientId, updatedClient);
    }

    @Override
    public Optional<Client> deleteClient(UUID clientId) throws SQLException {
        return clientRepository.deleteClient(clientId);
    }
}

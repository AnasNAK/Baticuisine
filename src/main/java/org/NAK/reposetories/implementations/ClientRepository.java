package org.NAK.reposetories.implementations;

import org.NAK.db.Database;
import org.NAK.entities.Client;
import org.NAK.reposetories.contracts.ClientInterface;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository implements ClientInterface  {

    private Connection connection;

    public ClientRepository(){
        this.connection = Database.getInstance().establishConnection();
    }

    @Override
    public void addClient(Client client) throws SQLException{
        String sql = "INSERT INTO client ( name, address, telephone, isProfessional) VALUES ( ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            stmt.setObject(1,client.getName());
            stmt.setObject(2,client.getAdress());
            stmt.setObject(3,client.getTelephone());
            stmt.setObject(4,client.getProfessional());
          int affectedRows =  stmt.executeUpdate();

          if (affectedRows == 0){
              throw new SQLException("Adding client failed, no rows affected.");
          }

          try(ResultSet rs = stmt.getGeneratedKeys()){
              if (rs.next()){
                  client.setId((UUID) rs.getObject(1));
              }else {
                  throw new SQLException("Adding client failed, no ID obtained.");
              }

          }

        }
    }

    @Override
    public Optional<Client> findClientById(UUID clientId) throws SQLException{
        String sql = "SELECT * FROM client WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setObject(1,clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Client client = mapResultSetToClient(rs);
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> getAllClients() throws SQLException{
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    @Override
    public Optional<Client> updateClient(UUID clientId , Client updatedClient) throws  SQLException{
        String sql = "UPDATE client SET name = ?, address = ? ,telephone = ? ,isprofessional = ? WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,updatedClient.getName());
            stmt.setString(1,updatedClient.getAdress());
            stmt.setString(3,updatedClient.getTelephone());
            stmt.setBoolean(4,updatedClient.getProfessional());
            stmt.setObject(5,clientId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0){
                return Optional.of(updatedClient);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> deleteClient(UUID clientId) throws SQLException{
        String sql = "DELETE FROM client WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setObject(1,clientId);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0){
                return Optional.of(new Client());
            }
        }
        return Optional.empty();
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId((UUID) rs.getObject("id"));
        client.setName(rs.getString("name"));
        client.setAdress(rs.getString("address"));
        client.setTelephone(rs.getString("telephone"));
        client.setProfessional(rs.getBoolean("isprofessional"));
        return client;
    }
}

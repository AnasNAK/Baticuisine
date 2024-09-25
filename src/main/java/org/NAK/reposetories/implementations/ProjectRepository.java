package org.NAK.reposetories.implementations;


import org.NAK.db.Database;
import org.NAK.entities.Client;
import org.NAK.entities.Estimate;
import org.NAK.entities.Project;
import org.NAK.enums.ProjectState;
import org.NAK.reposetories.contracts.ProjectInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ProjectRepository implements ProjectInterface{

    private Connection connection;

    public ProjectRepository(){
        this.connection = Database.getInstance().establishConnection();
    }

    @Override
    public void addProject(Project project) throws SQLException {
        String sql = "INSERT INTO project (name, profitmargin, totalcost, projectstate, clientid) VALUES (?, ?, ?, ?::projectstate, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, project.getProjectName());
            stmt.setDouble(2, project.getProfitMargin());
            stmt.setDouble(3, project.getTotalCost());
            stmt.setString(4, project.getProjectState().name());
            stmt.setObject(5, project.getClient().getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Adding project failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    project.setId((UUID) rs.getObject(1));
                    System.out.println("Generated Project ID: " + project.getId());
                } else {
                    throw new SQLException("Adding project failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public Optional<Project> findProjectById(UUID projectId) throws SQLException {
        String sql = "SELECT p.*, c.*, e.* FROM project p " +
                "JOIN client c ON p.client_id = c.id " +
                "LEFT JOIN estimate e ON p.id = e.project_id " +
                "WHERE p.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Project project = new Project();
                    project.setId(UUID.fromString(rs.getString("p.id")));
                    project.setProjectName(rs.getString("p.name"));
                    project.setProfitMargin(rs.getDouble("p.profitmargin"));
                    project.setTotalCost(rs.getDouble("p.totalcost"));
                    project.setProjectState(ProjectState.valueOf(rs.getString("p.projectstate")));

                    Client client = new Client();
                    client.setId(UUID.fromString(rs.getString("c.id")));
                    client.setName(rs.getString("c.name"));
                    client.setAdress(rs.getString("c.address"));
                    client.setTelephone(rs.getString("c.telephone"));
                    client.setProfessional(rs.getBoolean("c.isprofessional"));

                    project.setClient(client);

                    List<Estimate> estimates = new ArrayList<>();
                    do {
                        Estimate estimate = new Estimate();
                        estimate.setId(UUID.fromString(rs.getString("e.id")));
                        estimate.setEstimatedAmount(rs.getDouble("e.estimatedamount"));
                        estimate.setEstimatedDate(rs.getObject("e.estimateddate", LocalDate.class));
                        estimate.setValidityDate(rs.getObject("e.validitydate", LocalDate.class));
                        estimate.setAccepted(rs.getBoolean("e.accepted"));

                        estimate.setProject(project);

                        estimates.add(estimate);
                    } while (rs.next());

                    project.setEstimates(estimates);

                    return Optional.of(project);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Project> getAllProjects() throws SQLException {
        String sql = "SELECT p.*, c.*, e.* FROM project p " +
                "JOIN client c ON c.id = p.clientid " +
                "LEFT JOIN estimate e ON e.projectid = p.id";

        Map<UUID, Client> clientMap = new HashMap<>();
        Map<UUID, Estimate> estimateMap = new HashMap<>();
        List<Project> projectsList = new ArrayList<>();
        Map<UUID, Project> projectMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                UUID projectId = rs.getObject("p.id", UUID.class);
                Project project = projectMap.get(projectId);

                if (project == null) {
                    project = new Project();
                    project.setId(projectId);
                    project.setProjectName(rs.getString("p.project_name"));
                    project.setProfitMargin(rs.getDouble("p.profit_margin"));
                    project.setTotalCost(rs.getDouble("p.total_cost"));
                    project.setProjectState(ProjectState.valueOf(rs.getString("p.project_state")));

                    UUID clientId = rs.getObject("c.id", UUID.class);
                    if (clientId != null) {
                        Client client = clientMap.get(clientId);
                        if (client == null) {
                            client = new Client();
                            client.setId(clientId);
                            client.setName(rs.getString("c.name"));
                            client.setAdress(rs.getString("c.address"));
                            client.setTelephone(rs.getString("c.telephone"));
                            client.setProfessional(rs.getBoolean("c.isProfessional"));

                            clientMap.put(clientId, client);
                        }
                        project.setClient(client);
                    }

                    projectMap.put(projectId, project);
                }

                UUID estimateId = rs.getObject("e.id", UUID.class);
                if (estimateId != null) {
                    Estimate estimate = estimateMap.get(estimateId);
                    if (estimate == null) {
                        estimate = new Estimate();
                        estimate.setId(estimateId);
                        estimate.setEstimatedAmount(rs.getDouble("e.estimated_amount"));
                        estimate.setEstimatedDate(rs.getObject("e.estimated_date", LocalDate.class));
                        estimate.setValidityDate(rs.getObject("e.validity_date", LocalDate.class));
                        estimate.setAccepted(rs.getBoolean("e.accepted"));
                        estimate.setProject(project);

                        estimateMap.put(estimateId, estimate);
                        project.getEstimates().add(estimate);
                    }
                }

                if (!projectsList.contains(project)) {
                    projectsList.add(project);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectsList;
    }







}

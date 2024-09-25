package org.NAK.subMenu;

import org.NAK.entities.Client;
import org.NAK.entities.Project;
import org.NAK.enums.ProjectState;
import org.NAK.services.implementations.ClientService;
import org.NAK.services.implementations.ProjectService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class ProjectUi {
    private Scanner scanner;
    private ClientService clientService;
    private ProjectService projectService;


    public ProjectUi(Scanner scanner) {
        this.scanner = scanner;
        this.clientService = new ClientService();
        this.projectService = new ProjectService();
    }

    public void createNewProject() {
        int choice;

        do {
            System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
            System.out.println("1. Chercher un client existant");
            System.out.println("2. Ajouter un nouveau client");
            System.out.println("3. Retour au menu principal");
            System.out.print("Choisissez une option : ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        searchExistingClient();
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de la recherche du client : " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        addNewClient();
                    } catch (SQLException e) {
                        System.out.println("Erreur lors de l'ajout du client : " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Retour au menu principal...");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        } while (choice != 3);
    }

    private void searchExistingClient() throws SQLException {
        System.out.print("Entrez l'ID du client à rechercher (UUID) : ");
        String clientIdInput = scanner.next();
        UUID clientId = UUID.fromString(clientIdInput);

        Optional<Client> clientOptional = clientService.findClientById(clientId);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            System.out.println("Client trouvé !");
            System.out.println("Nom : " + client.getName());
            System.out.println("Adresse : " + client.getAdress());
            System.out.println("Numéro de téléphone : " + client.getTelephone());
            if (client.getProfessional()) {
                System.out.println("Ce client est professionnel.");
            } else {
                System.out.println("Ce client n'est pas professionnel.");
            }

            System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
            char continueChoice = scanner.next().charAt(0);

            if (continueChoice == 'y' || continueChoice == 'Y') {
                createProjectWithClient(clientId);
            } else {
                System.out.println("Retour au menu précédent...");
            }
        } else {
            System.out.println("Client non trouvé.");
        }
    }

    private void createProjectWithClient(UUID clientId) {
        try {
            System.out.print("Entrez le nom du projet : ");
            String projectName = scanner.next();

            System.out.print("Entrez la marge bénéficiaire du projet : ");
            double profitMargin = scanner.nextDouble();


            System.out.print("Entrez l'état du projet (PENDING, COMPLETED, CANCELLED) : ");
            String projectStateInput = scanner.next();

            ProjectState projectState;
            try {
                projectState = ProjectState.valueOf(projectStateInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("État de projet invalide. Veuillez entrer PENDING, COMPLETED ou CANCELLED.");
                return;
            }

            Project project = new Project();
            project.setProjectName(projectName);
            project.setProfitMargin(profitMargin);
            project.setProjectState(projectState);

            Optional<Client> clientOptional = clientService.findClientById(clientId);
            if (clientOptional.isPresent()) {
                project.setClient(clientOptional.get());
                projectService.addProject(project);
                System.out.println("Projet créé avec succès !");
            } else {
                System.out.println("Client non trouvé pour l'ID fourni.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du projet : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Erreur de saisie. Veuillez vérifier les types de données.");
            scanner.nextLine(); // Clear the invalid input
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet : " + e.getMessage());
        }
    }


    private void addNewClient() throws SQLException {
        System.out.print("Entrez le nom du client : ");
        String name = scanner.next();

        System.out.print("Entrez l'adresse du client : ");
        String address = scanner.next();

        System.out.print("Entrez le numéro de téléphone du client : ");
        String telephone = scanner.next();

        System.out.print("Le client est-il professionnel ? (true/false) : ");
        boolean isProfessional = scanner.nextBoolean();

        Client newClient = new Client();
        newClient.setName(name);
        newClient.setAdress(address);
        newClient.setTelephone(telephone);
        newClient.setProfessional(isProfessional);

        clientService.addClient(newClient);
        System.out.println("Client ajouté avec succès !");
        System.out.println(newClient.getId());

        System.out.print("Souhaitez-vous continuer avec ce client pour créer un projet ? (y/n) : ");
        String response = scanner.next();

        if (response.equalsIgnoreCase("y")) {
            System.out.println("Création d'un projet pour ce client...");

        } else {
            System.out.println("Retour au menu précédent...");
        }
    }

}

package org.NAK.subMenu;

import org.NAK.entities.*;
import org.NAK.enums.ProjectState;
import org.NAK.services.implementations.ClientService;
import org.NAK.services.implementations.ProjectService;

import java.sql.SQLException;
import java.util.*;

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

            System.out.print("ajouter un material \n");

            Project project = new Project();
            project.setProjectName(projectName);
            project.setProjectState(ProjectState.PENDING);

            Optional<Client> clientOptional = clientService.findClientById(clientId);
            if (clientOptional.isPresent()) {
                project.setClient(clientOptional.get());

                List<Material> materials = addMaterials();

                System.out.print("ajouter une main-d'œuvre \n");

                List<WorkForce> workforces = addWorkForces();

                double tvaRate = askForTvaRate();

                double profitMargin = askForProfitMargin();

                project.setProfitMargin(profitMargin);

                applyTva(materials, tvaRate);
                applyTva(workforces, tvaRate);

                project.setMaterials(materials);
                project.setWorkForces(workforces);



                projectService.addProject(project);
                System.out.println("votre projet est creer avec succes");
                calculateTotalCost(project);



            } else {
                System.out.println("Client non trouvé pour l'ID fourni.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du projet : " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Erreur de saisie. Veuillez vérifier les types de données.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet : " + e.getMessage());
        }
    }

    private double askForTvaRate() {
        System.out.print("Souhaitez-vous appliquer une TVA ? (y/n) : ");
        char tvaChoice = scanner.next().charAt(0);
        if (tvaChoice == 'y' || tvaChoice == 'Y') {
            System.out.print("Entrez le pourcentage de TVA (e.g., 1.1 pour 10%) : ");
            return scanner.nextDouble();
        }
        return 1.0;
    }

    private double askForProfitMargin() {
        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire ? (y/n) : ");
        char profitChoice = scanner.next().charAt(0);
        if (profitChoice == 'y' || profitChoice == 'Y') {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (e.g., 1.1 pour 10%) : ");
            return scanner.nextDouble();
        }
        return 1.0;
    }

    private void applyTva(List<? extends Component> components, double tvaRate) {
       components.stream()
               .forEach(component -> component.setTvaRate(tvaRate));
    }

    private List<Material> addMaterials() {
        List<Material> materials = new ArrayList<>();
        MaterialUi materialUI = new MaterialUi(scanner);

        while (true) {
            Material material = materialUI.addMaterial();
            materials.add(material);

            System.out.print("Souhaitez-vous ajouter un autre matériau ? (y/n) : ");
            char choice = scanner.next().charAt(0);
            if (choice != 'y' && choice != 'Y') {
                break;
            }
        }
        return materials;
    }

    private List<WorkForce> addWorkForces() {
        List<WorkForce> workForces = new ArrayList<>();
        WorkForceUi workForceUi = new WorkForceUi(scanner);

        while (true) {
            WorkForce workForce = workForceUi.addWorkForce();
            workForces.add(workForce);

            System.out.print("Souhaitez-vous ajouter une autre main-d'œuvre ? (y/n) : ");
            char choice = scanner.next().charAt(0);
            if (choice != 'y' && choice != 'Y') {
                break;
            }
        }
        return workForces;
    }

    private static void calculateTotalCost(Project project) {
        if (project != null) {
            System.out.println("--- Calcul du coût total ---");

            double totalMaterialCostWithoutTva = project.getMaterials().stream()
                    .mapToDouble(material -> {
                        double baseCost = material.getQuantity() * material.getUnitaryCost();
                        double transportCost = material.getTransportCost();
                        return (baseCost * material.getOutputFactor()) + transportCost;
                    })
                    .sum();

            double totalWorkForceCostWithoutTva = project.getWorkForces().stream()
                    .mapToDouble(workforce -> {
                        double baseCost = workforce.getQuantity() * workforce.getUnitaryCost();
                        return baseCost * workforce.getOutputFactor();
                    })
                    .sum();

            double tvaRate = project.getMaterials().getFirst().getTvaRate();
            double profitMargin = project.getProfitMargin();

            double totalMaterialCostWithTva = totalMaterialCostWithoutTva *  tvaRate;
            double totalWorkForceCostWithTva = totalWorkForceCostWithoutTva * tvaRate;

            double totalCostWithoutProfit = totalMaterialCostWithTva + totalWorkForceCostWithTva;
            double profit = totalCostWithoutProfit * profitMargin;
            double totalFinalCost = totalCostWithoutProfit + profit;

            System.out.println("--- Résultat du Calcul ---");
            System.out.println("Nom du projet : " + project.getProjectName());
            System.out.println("Client : " + project.getClient().getName());
            System.out.println("Adresse du chantier : " + project.getClient().getAdress());
            System.out.println("--- Détail des Coûts ---");

            System.out.println("1. Matériaux :");
            project.getMaterials().forEach(material -> {
                double baseCost = material.getQuantity() * material.getUnitaryCost();
                double transportCost = material.getTransportCost();
                double totalCost = (baseCost * material.getOutputFactor()) + transportCost;
                System.out.printf("- %s : %.2f € (quantité : %.2f, coût unitaire : %.2f €, qualité : %.2f, transport : %.2f €)%n",
                        material.getName(), totalCost, material.getQuantity(), material.getUnitaryCost(),
                        material.getOutputFactor(), transportCost);
            });
            System.out.printf("**Coût total des matériaux avant TVA : %.2f €**%n", totalMaterialCostWithoutTva);
            System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**%n", tvaRate * 100, totalMaterialCostWithTva);

            System.out.println("2. Main-d'œuvre :");
            project.getWorkForces().forEach(workforce -> {
                double baseCost = workforce.getQuantity() * workforce.getUnitaryCost();
                double totalCost = baseCost * workforce.getOutputFactor();
                System.out.printf("- %s : %.2f € (taux horaire : %.2f €, heures travaillées : %.2f h, productivité : %.2f)%n",
                        workforce.getName(), totalCost, workforce.getUnitaryCost(), workforce.getQuantity(),
                        workforce.getOutputFactor());
            });
            System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**%n", totalWorkForceCostWithoutTva);
            System.out.printf("**Coût total de la main-d'œuvre avec TVA (%.0f%%) : %.2f €**%n", tvaRate * 100, totalWorkForceCostWithTva);

            System.out.printf("3. Coût total avant marge : %.2f €%n", totalCostWithoutProfit);
            System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €%n", profitMargin * 100, profit);
            System.out.printf("**Coût total final du projet : %.2f €**%n", totalFinalCost);

            Scanner scanner = new Scanner(System.in);
            System.out.print("--- Enregistrement du Devis ---\nEntrez la date d'émission du devis (format : jj/mm/aaaa) : ");
            String issueDate = scanner.next();
            System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
            String validityDate = scanner.next();
            System.out.print("Souhaitez-vous enregistrer le devis ? (y/n) : ");
            char saveChoice = scanner.next().charAt(0);

//            if (saveChoice == 'y' || saveChoice == 'Y') {
//                // Assuming a method exists to save the project and associated estimate
//                projectService.saveProjectWithEstimate(project, issueDate, validityDate);
//                System.out.println("Devis enregistré avec succès !");
//            }

            System.out.println("--- Fin du projet ---");
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

        UUID clientId = newClient.getId();

        System.out.print("Souhaitez-vous continuer avec ce client pour créer un projet ? (y/n) : ");
        char response = scanner.next().charAt(0);

        if (response == 'y' || response == 'Y') {
            createProjectWithClient(clientId);
        } else {
            System.out.println("Retour au menu précédent...");
        }
    }

}

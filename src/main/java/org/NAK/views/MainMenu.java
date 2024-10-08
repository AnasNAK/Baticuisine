package org.NAK.views;

import org.NAK.subMenu.ProjectUi;

import java.util.Scanner;

public class MainMenu {

    public void displayMenu(){

        Scanner scanner = new Scanner(System.in);
        ProjectUi projectUi = new ProjectUi(scanner);



        int choice;
        do {
            System.out.println("=== Menu Principal ===");
            System.out.println("1. Créer un nouveau projet");
            System.out.println("2. Afficher les projets existants");
            System.out.println("3. Calculer le coût d'un projet");
            System.out.println("4. Quitter");
            System.out.print("Choisissez une option : ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                  projectUi.createNewProject();
                    break;
                case 2:
//                    displayExistingProjects();

                    break;
                case 3:
//                    calculateProjectCost(scanner);
                    break;
                case 4:
                    System.out.println("Quitter...");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        } while (choice != 4);

        scanner.close();
    }
}

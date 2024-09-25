package org.NAK.subMenu;

import org.NAK.entities.WorkForce;
import org.NAK.enums.ComponentType;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class WorkForceUi {

    private final Scanner scanner;

    public WorkForceUi(Scanner scanner) {
        this.scanner = scanner;
    }

    public WorkForce addWorkForce() {
        WorkForce workForce = new WorkForce();
        workForce.setId(UUID.randomUUID());

        System.out.print("Entrez le nom de la main-d'œuvre : ");
        String name = scanner.next();
        workForce.setName(name);
        workForce.setComponentType(ComponentType.WORKFORCE);




        try {
            System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double unitaryCost = scanner.nextDouble();
            workForce.setUnitaryCost(unitaryCost);


            System.out.print("Entrez le nombre d'heures travaillées :");
            double quantity = scanner.nextDouble();
            workForce.setQuantity(quantity);

            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) :  ");
            double outputFactor = scanner.nextDouble();
            workForce.setOutputFactor(outputFactor);

        } catch (InputMismatchException e) {
            System.out.println("Erreur de saisie. Veuillez vérifier les types de données.");
            scanner.nextLine();
        }

        return workForce;
    }
}

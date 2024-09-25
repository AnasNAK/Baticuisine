package org.NAK.subMenu;

import org.NAK.entities.Material;
import org.NAK.enums.ComponentType;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class MaterialUi {

    private final Scanner scanner;

    public MaterialUi(Scanner scanner) {
        this.scanner = scanner;
    }

    public Material addMaterial() {
        Material material = new Material();
        material.setId(UUID.randomUUID());

        System.out.print("Entrez le nom du matériau : ");
        String name = scanner.next();
        material.setName(name);
        material.setComponentType(ComponentType.MATERIAL);



        try {
            System.out.print("Entrez le coût unitaire : ");
            double unitaryCost = scanner.nextDouble();
            material.setUnitaryCost(unitaryCost);

            System.out.print("Entrez la quantité : ");
            double quantity = scanner.nextDouble();
            material.setQuantity(quantity);

            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) :");
            double outputFactor = scanner.nextDouble();
            material.setOutputFactor(outputFactor);

            System.out.print("Entrez le coût de transport : ");
            double transportCost = scanner.nextDouble();
            material.setTransportCost(transportCost);
        } catch (InputMismatchException e) {
            System.out.println("Erreur de saisie. Veuillez vérifier les types de données.");
            scanner.nextLine();
        }

        return material;
    }
}

package com.company;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        // Scanner methods
        System.out.println("Insert home latitude");
        double homeLat = scanner.nextDouble(); scanner.nextLine();
        System.out.println("Insert home longitude");
        double homeLong = scanner.nextDouble(); scanner.nextLine();


        // Testing code
        Helicopter helicopter = new Helicopter("Helicopter-X",homeLat,homeLong);
        helicopter.startTravel();


    }

}

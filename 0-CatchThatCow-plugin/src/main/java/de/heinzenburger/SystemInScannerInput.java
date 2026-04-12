package de.heinzenburger;

import java.util.Scanner;

public class SystemInScannerInput implements TextInput {
    Scanner scanner = new Scanner(System.in);

    @Override
    public int readInt(int min, int max) {
        Integer input = null;
        while (input == null || input < min || input > max) {
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                if (input < min || input > max) {
                    System.out.println("Invalid input. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
                scanner.next(); // consume the invalid input
            }
        }
        return input;
    }
}

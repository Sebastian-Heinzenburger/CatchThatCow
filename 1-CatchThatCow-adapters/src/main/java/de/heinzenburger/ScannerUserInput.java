package de.heinzenburger;

import java.util.Scanner;

public class ScannerUserInput implements UserInput {
    private final Scanner scanner;

    public ScannerUserInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readLine() {
        return scanner.nextLine().trim();
    }

    @Override
    public int readInt(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Bitte eine Zahl zwischen " + min + " und " + max + " eingeben.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe! Bitte eine Zahl eingeben.");
            }
        }
    }
}

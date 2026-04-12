package de.heinzenburger;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class SystemInScannerInput implements TextInput {
    Scanner scanner = new Scanner(System.in);

    @Override
    public char readChar(char... allowedChars) {
        String input = null;
        List<String> allowedCharsList = Stream.of(allowedChars).map(String::valueOf).toList();
        while (input == null || !allowedCharsList.contains(input)) {
            if (scanner.hasNext()) {
                input = scanner.next();
                if (!allowedCharsList.contains(input)) {
                    System.out.println("Invalid input. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
                scanner.next();
            }
        }
        return input.charAt(0);
    }

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

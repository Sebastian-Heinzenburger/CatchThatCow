package de.heinzenburger.ui.parser;

import de.heinzenburger.shared.Direction;
import de.heinzenburger.shared.StatCategory;

import java.util.Optional;

/**
 * Centralized input parsing utility for command handling.
 * Eliminates duplicated parsing logic across handlers.
 */
public final class CommandParser {

    private CommandParser() {
    }

    /**
     * Tokenizes input into lowercase parts split by whitespace.
     * @return empty array if input is null/blank
     */
    public static String[] tokenize(String input) {
        if (input == null || input.isBlank()) {
            return new String[0];
        }
        return input.trim().toLowerCase().split("\\s+");
    }

    /**
     * Parses a direction from user input.
     * Supports full names and single-letter abbreviations.
     */
    public static Optional<Direction> parseDirection(String input) {
        if (input == null) return Optional.empty();
        return switch (input.toLowerCase()) {
            case "n", "north" -> Optional.of(Direction.NORTH);
            case "e", "east" -> Optional.of(Direction.EAST);
            case "s", "south" -> Optional.of(Direction.SOUTH);
            case "w", "west" -> Optional.of(Direction.WEST);
            default -> Optional.empty();
        };
    }

    /**
     * Parses a stat category from user input.
     * Supports full names and various abbreviations.
     */
    public static Optional<StatCategory> parseStatCategory(String input) {
        if (input == null) return Optional.empty();
        return switch (input.toLowerCase()) {
            case "speed", "spd", "sp" -> Optional.of(StatCategory.SPEED);
            case "length", "len", "l" -> Optional.of(StatCategory.LENGTH);
            case "weight", "wgt", "w" -> Optional.of(StatCategory.WEIGHT);
            case "lifespan", "life", "lf" -> Optional.of(StatCategory.LIFESPAN);
            case "offspring", "off", "o" -> Optional.of(StatCategory.OFFSPRING);
            default -> Optional.empty();
        };
    }

    /**
     * Parses a 1-based animal index from user input.
     * @return 0-based index, or -1 if invalid
     */
    public static int parseAnimalIndex(String input) {
        if (input == null) return -1;
        try {
            int index = Integer.parseInt(input);
            return index - 1; // Convert to 0-based
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Checks if input is empty after tokenization.
     */
    public static boolean isEmpty(String[] tokens) {
        return tokens.length == 0 || tokens[0].isEmpty();
    }

    /**
     * Gets the command (first token) from tokenized input.
     */
    public static String getCommand(String[] tokens) {
        return tokens.length > 0 ? tokens[0] : "";
    }
}

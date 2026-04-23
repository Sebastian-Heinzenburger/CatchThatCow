package de.heinzenburger.ui.command;

import java.util.Set;

/**
 * Constants for command aliases used in handlers.
 * Centralizes magic strings for command parsing.
 */
public final class Commands {

    private Commands() {
    }

    // Movement commands
    public static final Set<String> MOVE = Set.of("move", "m");
    public static final Set<String> NORTH = Set.of("n", "north");
    public static final Set<String> EAST = Set.of("e", "east");
    public static final Set<String> SOUTH = Set.of("s", "south");
    public static final Set<String> WEST = Set.of("w", "west");

    // Battle commands
    public static final Set<String> ATTACK = Set.of("attack", "a");
    public static final Set<String> DEFEND = Set.of("defend", "d");
    public static final Set<String> STATUS = Set.of("status", "st");

    // Encounter commands
    public static final Set<String> FIGHT = Set.of("fight", "f", "battle", "b");
    public static final Set<String> FLEE = Set.of("flee", "run", "r");

    // General commands
    public static final Set<String> HELP = Set.of("help", "h", "?");
    public static final Set<String> QUIT = Set.of("quit", "q", "exit");
    public static final Set<String> ENCOUNTER = Set.of("encounter", "enc");
    public static final Set<String> INVENTORY = Set.of("inventory", "inv", "i");
    public static final Set<String> MAP = Set.of("map");
    public static final Set<String> SAVE = Set.of("save");

    /**
     * Checks if the command matches any of the aliases.
     */
    public static boolean matches(String command, Set<String> aliases) {
        return command != null && aliases.contains(command.toLowerCase());
    }
}

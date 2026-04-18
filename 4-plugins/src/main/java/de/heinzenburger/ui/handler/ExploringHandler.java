package de.heinzenburger.ui.handler;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.exception.InvalidMoveException;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.shared.Direction;
import de.heinzenburger.shared.Position;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.usecase.MovePlayerUseCase;
import de.heinzenburger.usecase.SaveGameUseCase;
import de.heinzenburger.usecase.TriggerEncounterUseCase;
import de.heinzenburger.world.Biome;

import java.util.List;

/**
 * Handles commands during the EXPLORING phase.
 * Commands: move <direction>, encounter, inventory, map, save, help, quit
 */
public class ExploringHandler implements PhaseHandler {

    private final MovePlayerUseCase movePlayerUseCase;
    private final TriggerEncounterUseCase triggerEncounterUseCase;
    private final SaveGameUseCase saveGameUseCase;

    public ExploringHandler(
            MovePlayerUseCase movePlayerUseCase,
            TriggerEncounterUseCase triggerEncounterUseCase,
            SaveGameUseCase saveGameUseCase) {
        this.movePlayerUseCase = movePlayerUseCase;
        this.triggerEncounterUseCase = triggerEncounterUseCase;
        this.saveGameUseCase = saveGameUseCase;
    }

    @Override
    public CommandResult handle(String input, GameSession session) {
        String[] parts = input.trim().toLowerCase().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return new CommandResult.Error("Please enter a command. Type 'help' for available commands.");
        }

        String command = parts[0];

        return switch (command) {
            case "move", "m" -> handleMove(parts, session);
            case "n", "north" -> handleMoveDirection(Direction.NORTH, session);
            case "e", "east" -> handleMoveDirection(Direction.EAST, session);
            case "s", "south" -> handleMoveDirection(Direction.SOUTH, session);
            case "w", "west" -> handleMoveDirection(Direction.WEST, session);
            case "encounter", "enc" -> handleEncounter();
            case "inventory", "inv", "i" -> handleInventory(session);
            case "map" -> handleMap(session);
            case "save" -> handleSave();
            case "help", "h", "?" -> new CommandResult.Help(getAvailableCommands());
            case "quit", "q", "exit" -> new CommandResult.Quit();
            default -> new CommandResult.Error("Unknown command: " + command + ". Type 'help' for available commands.");
        };
    }

    private CommandResult handleMove(String[] parts, GameSession session) {
        if (parts.length < 2) {
            return new CommandResult.Error("Usage: move <n|e|s|w> or use shortcut: n/e/s/w");
        }

        Direction direction = parseDirection(parts[1]);
        if (direction == null) {
            return new CommandResult.Error("Invalid direction: " + parts[1] + ". Use n/e/s/w or north/east/south/west");
        }

        return handleMoveDirection(direction, session);
    }

    private CommandResult handleMoveDirection(Direction direction, GameSession session) {
        try {
            Position newPos = movePlayerUseCase.execute(direction);
            Biome biome = session.getWorld().getBiomeAt(newPos);
            return new CommandResult.MovedTo(newPos, biome);
        } catch (InvalidMoveException e) {
            return new CommandResult.Error("Cannot move there - you've reached the edge of the world!");
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        }
    }

    private CommandResult handleEncounter() {
        try {
            Animal animal = triggerEncounterUseCase.execute();
            return new CommandResult.EncounterStarted(animal);
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        }
    }

    private CommandResult handleInventory(GameSession session) {
        List<Animal> animals = session.getPlayer().getInventory().getAnimals();
        return new CommandResult.ShowInventory(animals);
    }

    private CommandResult handleMap(GameSession session) {
        return new CommandResult.ShowMap(session.getWorld(), session.getPlayer().getCurrentPosition());
    }

    private CommandResult handleSave() {
        try {
            saveGameUseCase.execute();
            return new CommandResult.GameSaved();
        } catch (GameNotStartedException e) {
            return new CommandResult.Error(e.getMessage());
        }
    }

    private Direction parseDirection(String dir) {
        return switch (dir.toLowerCase()) {
            case "n", "north" -> Direction.NORTH;
            case "e", "east" -> Direction.EAST;
            case "s", "south" -> Direction.SOUTH;
            case "w", "west" -> Direction.WEST;
            default -> null;
        };
    }

    @Override
    public List<String> getAvailableCommands() {
        return List.of(
                "move <n|e|s|w>  - Move in a direction (or just: n/e/s/w)",
                "encounter       - Search for a wild animal",
                "inventory       - View your animals",
                "map             - Show the world map",
                "save            - Save your game",
                "help            - Show this help",
                "quit            - Exit the game"
        );
    }
}

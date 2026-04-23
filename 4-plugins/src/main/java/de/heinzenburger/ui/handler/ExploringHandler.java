package de.heinzenburger.ui.handler;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.exception.InvalidMoveException;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.shared.Direction;
import de.heinzenburger.shared.Position;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.ui.command.Commands;
import de.heinzenburger.ui.dto.AnimalSummary;
import de.heinzenburger.ui.dto.MapData;
import de.heinzenburger.ui.dto.MovementResult;
import de.heinzenburger.ui.parser.CommandParser;
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
        String[] parts = CommandParser.tokenize(input);
        if (CommandParser.isEmpty(parts)) {
            return new CommandResult.Error("Please enter a command. Type 'help' for available commands.");
        }

        String command = CommandParser.getCommand(parts);

        if (Commands.matches(command, Commands.MOVE)) return handleMove(parts, session);
        if (Commands.matches(command, Commands.NORTH)) return handleMoveDirection(Direction.NORTH, session);
        if (Commands.matches(command, Commands.EAST)) return handleMoveDirection(Direction.EAST, session);
        if (Commands.matches(command, Commands.SOUTH)) return handleMoveDirection(Direction.SOUTH, session);
        if (Commands.matches(command, Commands.WEST)) return handleMoveDirection(Direction.WEST, session);
        if (Commands.matches(command, Commands.ENCOUNTER)) return handleEncounter();
        if (Commands.matches(command, Commands.INVENTORY)) return handleInventory(session);
        if (Commands.matches(command, Commands.MAP)) return handleMap(session);
        if (Commands.matches(command, Commands.SAVE)) return handleSave();
        if (Commands.matches(command, Commands.HELP)) return new CommandResult.Help(getAvailableCommands());
        if (Commands.matches(command, Commands.QUIT)) return new CommandResult.Quit();

        return new CommandResult.Error("Unknown command: " + command + ". Type 'help' for available commands.");
    }

    private CommandResult handleMove(String[] parts, GameSession session) {
        if (parts.length < 2) {
            return new CommandResult.Error("Usage: move <n|e|s|w> or use shortcut: n/e/s/w");
        }

        Direction direction = CommandParser.parseDirection(parts[1]).orElse(null);
        if (direction == null) {
            return new CommandResult.Error("Invalid direction: " + parts[1] + ". Use n/e/s/w or north/east/south/west");
        }

        return handleMoveDirection(direction, session);
    }

    private CommandResult handleMoveDirection(Direction direction, GameSession session) {
        try {
            Position newPos = movePlayerUseCase.execute(direction);
            Biome biome = session.getWorld().getBiomeAt(newPos);
            return new CommandResult.MovedTo(MovementResult.from(newPos, biome));
        } catch (InvalidMoveException e) {
            return new CommandResult.Error("Cannot move there - you've reached the edge of the world!");
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        }
    }

    private CommandResult handleEncounter() {
        try {
            Animal animal = triggerEncounterUseCase.execute();
            return new CommandResult.EncounterStarted(AnimalSummary.from(animal));
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        }
    }

    private CommandResult handleInventory(GameSession session) {
        List<AnimalSummary> animals = session.getPlayer().getInventory().getAnimals().stream()
                .map(AnimalSummary::from)
                .toList();
        return new CommandResult.ShowInventory(animals);
    }

    private CommandResult handleMap(GameSession session) {
        return new CommandResult.ShowMap(MapData.from(session.getWorld(), session.getPlayer().getCurrentPosition()));
    }

    private CommandResult handleSave() {
        try {
            saveGameUseCase.execute();
            return new CommandResult.GameSaved();
        } catch (GameNotStartedException e) {
            return new CommandResult.Error(e.getMessage());
        }
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

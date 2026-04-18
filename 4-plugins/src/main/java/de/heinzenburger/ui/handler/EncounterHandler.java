package de.heinzenburger.ui.handler;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.usecase.FleeEncounterUseCase;
import de.heinzenburger.usecase.StartBattleUseCase;

import java.util.List;

/**
 * Handles commands during the ENCOUNTER_PENDING phase.
 * Commands: fight, flee (prey only), help
 */
public class EncounterHandler implements PhaseHandler {

    private final StartBattleUseCase startBattleUseCase;
    private final FleeEncounterUseCase fleeEncounterUseCase;

    public EncounterHandler(StartBattleUseCase startBattleUseCase, FleeEncounterUseCase fleeEncounterUseCase) {
        this.startBattleUseCase = startBattleUseCase;
        this.fleeEncounterUseCase = fleeEncounterUseCase;
    }

    @Override
    public CommandResult handle(String input, GameSession session) {
        String command = input.trim().toLowerCase();

        if (command.isEmpty()) {
            return new CommandResult.Error("Please enter a command. Type 'help' for options.");
        }

        return switch (command) {
            case "fight", "f", "battle", "b" -> handleFight();
            case "flee", "run", "r" -> handleFlee(session);
            case "help", "h", "?" -> new CommandResult.Help(getAvailableCommands(session));
            default -> new CommandResult.Error("Unknown command: " + command + ". Type 'help' for options.");
        };
    }

    private CommandResult handleFight() {
        try {
            Battle battle = startBattleUseCase.execute();
            return new CommandResult.BattleStarted(battle);
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        } catch (InsufficientAnimalsException e) {
            return new CommandResult.Error("You don't have enough animals to battle!");
        }
    }

    private CommandResult handleFlee(GameSession session) {
        Animal encountered = session.getEncounteredAnimal();
        if (encountered != null && encountered.isPredator()) {
            return new CommandResult.Error("You cannot flee from a predator! You must fight.");
        }

        try {
            fleeEncounterUseCase.execute();
            return new CommandResult.FledEncounter();
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        } catch (FleeEncounterUseCase.CannotFleeFromPredatorException e) {
            return new CommandResult.Error("You cannot flee from a predator! You must fight.");
        }
    }

    @Override
    public List<String> getAvailableCommands() {
        return getAvailableCommands(null);
    }

    public List<String> getAvailableCommands(GameSession session) {
        boolean canFlee = session == null ||
                          session.getEncounteredAnimal() == null ||
                          session.getEncounteredAnimal().isPrey();

        if (canFlee) {
            return List.of(
                    "fight  - Start a battle with this animal",
                    "flee   - Run away (prey only)",
                    "help   - Show this help"
            );
        } else {
            return List.of(
                    "fight  - Start a battle with this animal",
                    "help   - Show this help",
                    "(Cannot flee from predators!)"
            );
        }
    }
}

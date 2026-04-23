package de.heinzenburger.ui.handler;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.NoMoreAnimalsAvailableException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.shared.StatCategory;
import de.heinzenburger.ui.command.CommandResult;
import de.heinzenburger.ui.command.Commands;
import de.heinzenburger.ui.dto.BattleOutcomeSummary;
import de.heinzenburger.ui.dto.BattleSummary;
import de.heinzenburger.ui.dto.RoundOutcomeSummary;
import de.heinzenburger.ui.parser.CommandParser;
import de.heinzenburger.usecase.DefendAgainstAttackUseCase;
import de.heinzenburger.usecase.PlayerAttackUseCase;
import de.heinzenburger.usecase.ResolveBattleUseCase;

import java.util.List;

/**
 * Handles commands during the IN_BATTLE phase.
 * Commands: attack <#> <stat>, defend <#>, status, help
 */
public class BattleHandler implements PhaseHandler {

    private final PlayerAttackUseCase playerAttackUseCase;
    private final DefendAgainstAttackUseCase defendAgainstAttackUseCase;
    private final ResolveBattleUseCase resolveBattleUseCase;

    public BattleHandler(
            PlayerAttackUseCase playerAttackUseCase,
            DefendAgainstAttackUseCase defendAgainstAttackUseCase,
            ResolveBattleUseCase resolveBattleUseCase) {
        this.playerAttackUseCase = playerAttackUseCase;
        this.defendAgainstAttackUseCase = defendAgainstAttackUseCase;
        this.resolveBattleUseCase = resolveBattleUseCase;
    }

    @Override
    public CommandResult handle(String input, GameSession session) {
        String[] parts = CommandParser.tokenize(input);
        if (CommandParser.isEmpty(parts)) {
            return new CommandResult.Error("Please enter a command. Type 'help' for options.");
        }

        String command = CommandParser.getCommand(parts);
        Battle battle = session.getCurrentBattle();

        if (Commands.matches(command, Commands.ATTACK)) return handleAttack(parts, battle);
        if (Commands.matches(command, Commands.DEFEND)) return handleDefend(parts, battle);
        if (Commands.matches(command, Commands.STATUS)) return new CommandResult.ShowBattleStatus(BattleSummary.from(battle));
        if (Commands.matches(command, Commands.HELP)) return new CommandResult.Help(getAvailableCommands(battle));
        if (Commands.matches(command, Commands.QUIT)) return new CommandResult.Quit();

        return new CommandResult.Error("Unknown command: " + command + ". Type 'help' for options.");
    }

    private CommandResult handleAttack(String[] parts, Battle battle) {
        if (!battle.isPlayerTurn()) {
            return new CommandResult.Error("It's not your turn to attack! Use 'defend <#>' to respond.");
        }

        if (parts.length < 3) {
            return new CommandResult.Error("Usage: attack <animal#> <stat>\nStats: speed, length, weight, lifespan, offspring");
        }

        int animalIndex = CommandParser.parseAnimalIndex(parts[1]);
        if (animalIndex < 0) {
            return new CommandResult.Error("Invalid animal number: " + parts[1]);
        }

        List<Animal> available = battle.getAvailableAnimals();
        if (animalIndex >= available.size()) {
            return new CommandResult.Error("Animal #" + (animalIndex + 1) + " not available. You have " + available.size() + " animals.");
        }

        StatCategory category = CommandParser.parseStatCategory(parts[2]).orElse(null);
        if (category == null) {
            return new CommandResult.Error("Invalid stat: " + parts[2] + "\nValid stats: speed, length, weight, lifespan, offspring");
        }

        Animal selectedAnimal = available.get(animalIndex);

        try {
            RoundResult result = playerAttackUseCase.execute(selectedAnimal, category);
            return checkBattleEnd(battle, result);
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        } catch (AnimalNotAvailableException e) {
            return new CommandResult.Error("That animal is not available: " + e.getMessage());
        }
    }

    private CommandResult handleDefend(String[] parts, Battle battle) {
        if (battle.isPlayerTurn()) {
            return new CommandResult.Error("It's your turn to attack! Use 'attack <#> <stat>'.");
        }

        if (parts.length < 2) {
            try {
                StatCategory opponentStat = defendAgainstAttackUseCase.getOpponentSelectedCategory();
                return new CommandResult.Error("Usage: defend <animal#>\nOpponent is attacking with: " + opponentStat);
            } catch (GameNotStartedException | InvalidGamePhaseException e) {
                return new CommandResult.Error("Usage: defend <animal#>");
            }
        }

        int animalIndex = CommandParser.parseAnimalIndex(parts[1]);
        if (animalIndex < 0) {
            return new CommandResult.Error("Invalid animal number: " + parts[1]);
        }

        List<Animal> available = battle.getAvailableAnimals();
        if (animalIndex >= available.size()) {
            return new CommandResult.Error("Animal #" + (animalIndex + 1) + " not available. You have " + available.size() + " animals.");
        }

        Animal selectedAnimal = available.get(animalIndex);

        try {
            RoundResult result = defendAgainstAttackUseCase.execute(selectedAnimal);
            return checkBattleEnd(battle, result);
        } catch (GameNotStartedException | InvalidGamePhaseException e) {
            return new CommandResult.Error(e.getMessage());
        } catch (AnimalNotAvailableException e) {
            return new CommandResult.Error("That animal is not available: " + e.getMessage());
        } catch (NoMoreAnimalsAvailableException e) {
            return new CommandResult.Error("No more animals available!");
        }
    }

    private CommandResult checkBattleEnd(Battle battle, RoundResult result) {
        if (battle.isFinished()) {
            try {
                var outcome = resolveBattleUseCase.execute();
                return new CommandResult.BattleEnded(BattleOutcomeSummary.from(outcome));
            } catch (GameNotStartedException | InvalidGamePhaseException e) {
                return new CommandResult.RoundOutcome(RoundOutcomeSummary.from(result), true);
            }
        }
        return new CommandResult.RoundOutcome(RoundOutcomeSummary.from(result), false);
    }

    @Override
    public List<String> getAvailableCommands() {
        return getAvailableCommands(null);
    }

    public List<String> getAvailableCommands(Battle battle) {
        boolean isPlayerTurn = battle == null || battle.isPlayerTurn();

        if (isPlayerTurn) {
            return List.of(
                    "attack <#> <stat>  - Attack with animal # using stat",
                    "                     Stats: speed, length, weight, lifespan, offspring",
                    "status             - Show battle status",
                    "help               - Show this help"
            );
        } else {
            return List.of(
                    "defend <#>         - Defend with animal #",
                    "status             - Show battle status",
                    "help               - Show this help"
            );
        }
    }
}

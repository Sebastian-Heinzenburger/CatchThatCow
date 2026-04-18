package de.heinzenburger.ui.command;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.shared.Position;
import de.heinzenburger.usecase.ResolveBattleUseCase.BattleOutcome;
import de.heinzenburger.world.Biome;

import java.util.List;

/**
 * Sealed interface representing outcomes of command execution.
 * Uses Strategy pattern - each result type knows how to display itself.
 */
public sealed interface CommandResult {

    default boolean shouldQuit() {
        return false;
    }

    record Success(String message) implements CommandResult {
    }

    record Error(String message) implements CommandResult {
    }

    record Help(List<String> commands) implements CommandResult {
    }

    record Quit() implements CommandResult {
        @Override
        public boolean shouldQuit() {
            return true;
        }
    }

    record MovedTo(Position position, Biome biome) implements CommandResult {
    }

    record EncounterStarted(Animal animal) implements CommandResult {
    }

    record BattleStarted(de.heinzenburger.battle.Battle battle) implements CommandResult {
    }

    record RoundOutcome(RoundResult result, boolean battleFinished) implements CommandResult {
    }

    record BattleEnded(BattleOutcome outcome) implements CommandResult {
    }

    record FledEncounter() implements CommandResult {
    }

    record GameSaved() implements CommandResult {
    }

    record ShowInventory(List<Animal> animals) implements CommandResult {
    }

    record ShowMap(de.heinzenburger.world.World world, Position playerPosition) implements CommandResult {
    }

    record ShowBattleStatus(de.heinzenburger.battle.Battle battle) implements CommandResult {
    }
}

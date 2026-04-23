package de.heinzenburger.ui.command;

import de.heinzenburger.ui.dto.*;

import java.util.List;

/**
 * Sealed interface representing outcomes of command execution.
 * Uses Strategy pattern - each result type knows how to display itself.
 * Uses presentation DTOs to decouple from domain entities.
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

    record MovedTo(MovementResult movement) implements CommandResult {
    }

    record EncounterStarted(AnimalSummary animal) implements CommandResult {
    }

    record BattleStarted(BattleSummary battle) implements CommandResult {
    }

    record RoundOutcome(RoundOutcomeSummary result, boolean battleFinished) implements CommandResult {
    }

    record BattleEnded(BattleOutcomeSummary outcome) implements CommandResult {
    }

    record FledEncounter() implements CommandResult {
    }

    record GameSaved() implements CommandResult {
    }

    record ShowInventory(List<AnimalSummary> animals) implements CommandResult {
    }

    record ShowMap(MapData map) implements CommandResult {
    }

    record ShowBattleStatus(BattleSummary battle) implements CommandResult {
    }
}

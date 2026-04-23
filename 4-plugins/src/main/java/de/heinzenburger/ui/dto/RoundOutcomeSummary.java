package de.heinzenburger.ui.dto;

import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.RoundWinner;
import de.heinzenburger.shared.StatCategory;

/**
 * Presentation DTO for displaying round results in the UI.
 * Decouples the view layer from domain entities.
 */
public record RoundOutcomeSummary(
        StatCategory category,
        String playerAnimalName,
        int playerStatValue,
        String opponentAnimalName,
        int opponentStatValue,
        boolean playerWon
) {
    public static RoundOutcomeSummary from(RoundResult result) {
        return new RoundOutcomeSummary(
                result.category(),
                result.playerAnimal() != null ? result.playerAnimal().getSpecies().name() : null,
                result.playerStatValue(),
                result.opponentAnimal().getSpecies().name(),
                result.opponentStatValue(),
                result.winner() == RoundWinner.PLAYER
        );
    }
}

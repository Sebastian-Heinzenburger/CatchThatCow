package de.heinzenburger.ui.dto;

import de.heinzenburger.battle.Battle;

import java.util.List;

/**
 * Presentation DTO for displaying battle state in the UI.
 * Decouples the view layer from domain entities.
 */
public record BattleSummary(
        int playerScore,
        int opponentScore,
        boolean isPlayerTurn,
        AnimalSummary opponent,
        List<AnimalSummary> availableAnimals
) {
    public static BattleSummary from(Battle battle) {
        return new BattleSummary(
                battle.getPlayerScore(),
                battle.getOpponentScore(),
                battle.isPlayerTurn(),
                AnimalSummary.from(battle.getOpponentAnimal()),
                battle.getAvailableAnimals().stream()
                        .map(AnimalSummary::from)
                        .toList()
        );
    }
}

package de.heinzenburger.ui.dto;

import de.heinzenburger.usecase.ResolveBattleUseCase.BattleOutcome;

/**
 * Presentation DTO for displaying battle outcome in the UI.
 * Decouples the view layer from domain entities.
 */
public record BattleOutcomeSummary(
        boolean playerWon,
        String caughtAnimalName,
        String lostAnimalName
) {
    public static BattleOutcomeSummary from(BattleOutcome outcome) {
        return new BattleOutcomeSummary(
                outcome.playerWon(),
                outcome.caughtAnimal() != null ? outcome.caughtAnimal().getSpecies().name() : null,
                outcome.lostAnimal() != null ? outcome.lostAnimal().getSpecies().name() : null
        );
    }
}

package de.heinzenburger;

import java.util.List;
import java.util.Map;

public class TerminalFightPresenter implements FightPresenter {
    private final TextPresenter textPresenter;
    private final UserInput userInput;

    public TerminalFightPresenter(TextPresenter textPresenter, UserInput userInput) {
        this.textPresenter = textPresenter;
        this.userInput = userInput;
    }

    @Override
    public void showEncounter(Animal wildAnimal) {
        textPresenter.print("");
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("  Ein wildes " + wildAnimal.getSpecies() + " erscheint!");
        textPresenter.print("  " + wildAnimal.getNoise());
        textPresenter.print("  Level: " + wildAnimal.getLevel());
        textPresenter.print("  Typ: " + (wildAnimal.getType() == AnimalType.PREDATOR ? "Raubtier" : "Fluchttier"));
        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("");
    }

    @Override
    public Animal selectAnimal(List<Animal> availableAnimals) {
        textPresenter.print("Wähle ein Tier für diese Runde:");

        for (int i = 0; i < availableAnimals.size(); i++) {
            Animal animal = availableAnimals.get(i);
            textPresenter.print("  " + (i + 1) + ". " + animal.getSpecies() + " (Level " + animal.getLevel() + ")");
            textPresenter.print("     Geschwindigkeit: " + animal.getStatValue(StatCategory.SPEED) +
                    ", Länge: " + animal.getStatValue(StatCategory.LENGTH) +
                    ", Gewicht: " + animal.getStatValue(StatCategory.WEIGHT) +
                    ", Stärke: " + animal.getStatValue(StatCategory.STRENGTH));
        }

        int choice = userInput.readInt(1, availableAnimals.size());
        return availableAnimals.get(choice - 1);
    }

    @Override
    public StatCategory selectCategory() {
        textPresenter.print("");
        textPresenter.print("Wähle eine Kategorie:");
        textPresenter.print("  1. Geschwindigkeit");
        textPresenter.print("  2. Länge");
        textPresenter.print("  3. Gewicht");
        textPresenter.print("  4. Stärke");

        int choice = userInput.readInt(1, 4);
        return StatCategory.values()[choice - 1];
    }

    @Override
    public void showRoundResult(FightResult result) {
        textPresenter.print("");
        textPresenter.print("--- Rundenergebnis ---");
        textPresenter.print("Kategorie: " + result.getCategory().getDisplayName());
        textPresenter.print("Dein " + result.getPlayerAnimal().getSpecies() + ": " + result.getPlayerAnimal().getStatValue(result.getCategory()));
        textPresenter.print("Wilder " + result.getEnemyAnimal().getSpecies() + ": " + result.getEnemyAnimal().getStatValue(result.getCategory()));

        if (result.isPlayerWon()) {
            textPresenter.print(">>> Du gewinnst diese Runde! <<<");
        } else {
            textPresenter.print(">>> Das wilde Tier gewinnt diese Runde! <<<");
        }

        textPresenter.print("Spielstand: " + result.getCurrentPlayerScore() + " : " + result.getCurrentEnemyScore());
        textPresenter.print("");
    }

    @Override
    public void showFightOutcome(FightOutcome outcome, Animal caughtOrLostAnimal) {
        textPresenter.print("");
        textPresenter.print("═══════════════════════════════════════");

        if (outcome == FightOutcome.PLAYER_WON) {
            textPresenter.print("  🎉 SIEG! 🎉");
            textPresenter.print("  Du hast " + caughtOrLostAnimal.getSpecies() + " gefangen!");
        } else {
            textPresenter.print("  💀 NIEDERLAGE! 💀");
            textPresenter.print("  Du hast " + caughtOrLostAnimal.getSpecies() + " verloren!");
        }

        textPresenter.print("═══════════════════════════════════════");
        textPresenter.print("");
    }
}

package de.heinzenburger;

import java.util.List;
import java.util.Random;

public class FightUseCase {
    private final FightPresenter presenter;
    private final int battleInventorySize = 3;
    private final int pointsToWin = 3;

    public FightUseCase(FightPresenter presenter) {
        this.presenter = presenter;
    }

    public void executeFight(Player player, Animal wildAnimal) {
        // Prüfe ob genug Tiere im Inventar
        if (player.getInventorySize() < battleInventorySize) {
            throw new IllegalStateException("Nicht genug Tiere im Inventar! Benötigt: " + battleInventorySize);
        }

        presenter.showEncounter(wildAnimal);

        // Erstelle Kampfinventar
        List<Animal> battleInventory = player.getRandomBattleInventory(battleInventorySize);
        Fight fight = new Fight(wildAnimal, battleInventory, pointsToWin);

        // Kampfloop
        while (!fight.isFinished()) {
            StatCategory category;
            Animal playerAnimal;

            // Erste Runde: Bei Raubtier greift das wilde Tier zuerst an
            if (fight.isFirstRound() && wildAnimal.getType() == AnimalType.PREDATOR) {
                category = getRandomCategory();
                playerAnimal = presenter.selectAnimal(fight.getAvailableAnimals());
            } else {
                // Spieler wählt Tier und Kategorie
                playerAnimal = presenter.selectAnimal(fight.getAvailableAnimals());
                category = presenter.selectCategory();
            }

            FightResult result = fight.playRound(playerAnimal, category);
            presenter.showRoundResult(result);
        }

        // Kampfergebnis
        FightOutcome outcome = fight.getOutcome();

        if (outcome == FightOutcome.PLAYER_WON) {
            player.addAnimal(wildAnimal);
            presenter.showFightOutcome(outcome, wildAnimal);
        } else {
            // Zufälliges Tier aus dem Kampfinventar verlieren
            Animal lostAnimal = battleInventory.get(new Random().nextInt(battleInventory.size()));
            player.removeAnimal(lostAnimal);
            presenter.showFightOutcome(outcome, lostAnimal);
        }
    }

    private StatCategory getRandomCategory() {
        StatCategory[] categories = StatCategory.values();
        return categories[new Random().nextInt(categories.length)];
    }
}

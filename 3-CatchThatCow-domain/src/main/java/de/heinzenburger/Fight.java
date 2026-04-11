package de.heinzenburger;

import java.util.ArrayList;
import java.util.List;

public class Fight {
    private final Animal wildAnimal;
    private final List<Animal> battleInventory;
    private final List<Animal> usedAnimals;
    private int playerScore;
    private int enemyScore;
    private final int pointsToWin;

    public Fight(Animal wildAnimal, List<Animal> battleInventory, int pointsToWin) {
        this.wildAnimal = wildAnimal;
        this.battleInventory = new ArrayList<>(battleInventory);
        this.usedAnimals = new ArrayList<>();
        this.playerScore = 0;
        this.enemyScore = 0;
        this.pointsToWin = pointsToWin;
    }

    public FightResult playRound(Animal playerAnimal, StatCategory category) {
        if (!battleInventory.contains(playerAnimal)) {
            throw new IllegalArgumentException("Tier nicht im Kampfinventar oder bereits verwendet!");
        }

        boolean playerWins = playerAnimal.winsAgainst(wildAnimal, category);

        if (playerWins) {
            playerScore++;
        } else {
            enemyScore++;
        }

        // Tier als verwendet markieren
        battleInventory.remove(playerAnimal);
        usedAnimals.add(playerAnimal);

        return new FightResult(playerAnimal, wildAnimal, category, playerWins, playerScore, enemyScore);
    }

    public boolean isFinished() {
        return playerScore >= pointsToWin || enemyScore >= pointsToWin;
    }

    public FightOutcome getOutcome() {
        if (!isFinished()) {
            throw new IllegalStateException("Kampf ist noch nicht beendet!");
        }
        return playerScore >= pointsToWin ? FightOutcome.PLAYER_WON : FightOutcome.PLAYER_LOST;
    }

    public Animal getWildAnimal() {
        return wildAnimal;
    }

    public List<Animal> getAvailableAnimals() {
        return new ArrayList<>(battleInventory);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }

    public int getRoundNumber() {
        return usedAnimals.size() + 1;
    }

    public boolean isFirstRound() {
        return usedAnimals.isEmpty();
    }
}

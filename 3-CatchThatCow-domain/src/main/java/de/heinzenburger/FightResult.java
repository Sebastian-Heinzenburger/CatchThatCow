package de.heinzenburger;

public class FightResult {
    private final Animal playerAnimal;
    private final Animal enemyAnimal;
    private final StatCategory category;
    private final boolean playerWon;
    private final int currentPlayerScore;
    private final int currentEnemyScore;

    public FightResult(Animal playerAnimal, Animal enemyAnimal, StatCategory category,
                       boolean playerWon, int currentPlayerScore, int currentEnemyScore) {
        this.playerAnimal = playerAnimal;
        this.enemyAnimal = enemyAnimal;
        this.category = category;
        this.playerWon = playerWon;
        this.currentPlayerScore = currentPlayerScore;
        this.currentEnemyScore = currentEnemyScore;
    }

    public Animal getPlayerAnimal() {
        return playerAnimal;
    }

    public Animal getEnemyAnimal() {
        return enemyAnimal;
    }

    public StatCategory getCategory() {
        return category;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public int getCurrentPlayerScore() {
        return currentPlayerScore;
    }

    public int getCurrentEnemyScore() {
        return currentEnemyScore;
    }

    public Animal getWinner() {
        return playerWon ? playerAnimal : enemyAnimal;
    }
}

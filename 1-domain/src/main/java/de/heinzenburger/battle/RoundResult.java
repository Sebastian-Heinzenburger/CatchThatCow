package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.StatCategory;

import java.util.Objects;

public record RoundResult(Animal playerAnimal, Animal opponentAnimal, StatCategory category, RoundWinner winner,
                          int playerStatValue, int opponentStatValue) {

    public RoundResult {
        if (opponentAnimal == null) throw new IllegalArgumentException("Opponent animal cannot be null");
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        if (winner == null) throw new IllegalArgumentException("Winner cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundResult that = (RoundResult) o;
        return playerStatValue == that.playerStatValue && opponentStatValue == that.opponentStatValue && Objects.equals(playerAnimal, that.playerAnimal) && Objects.equals(opponentAnimal, that.opponentAnimal) && category == that.category && winner == that.winner;
    }

    @Override
    public String toString() {
        return "RoundResult{" + "playerAnimal=" + playerAnimal + ", opponentAnimal=" + opponentAnimal + ", category=" + category + ", winner=" + winner + ", score=" + playerStatValue + " vs " + opponentStatValue + '}';
    }
}

package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.StatCategory;

import java.util.Objects;

public final class RoundResult {
    private final Animal playerAnimal;
    private final Animal opponentAnimal;
    private final StatCategory category;
    private final RoundWinner winner;
    private final int playerStatValue;
    private final int opponentStatValue;

    public RoundResult(Animal playerAnimal, Animal opponentAnimal, StatCategory category, RoundWinner winner, int playerStatValue, int opponentStatValue) {
        if (opponentAnimal == null) throw new IllegalArgumentException("Opponent animal cannot be null");
        if (category == null) throw new IllegalArgumentException("Category cannot be null");
        if (winner == null) throw new IllegalArgumentException("Winner cannot be null");

        this.playerAnimal = playerAnimal;
        this.opponentAnimal = opponentAnimal;
        this.category = category;
        this.winner = winner;
        this.playerStatValue = playerStatValue;
        this.opponentStatValue = opponentStatValue;
    }

    public Animal getPlayerAnimal() {
        return playerAnimal;
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    public StatCategory getCategory() {
        return category;
    }

    public RoundWinner getWinner() {
        return winner;
    }

    public int getPlayerStatValue() {
        return playerStatValue;
    }

    public int getOpponentStatValue() {
        return opponentStatValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundResult that = (RoundResult) o;
        return playerStatValue == that.playerStatValue && opponentStatValue == that.opponentStatValue && Objects.equals(playerAnimal, that.playerAnimal) && Objects.equals(opponentAnimal, that.opponentAnimal) && category == that.category && winner == that.winner;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerAnimal, opponentAnimal, category, winner, playerStatValue, opponentStatValue);
    }

    @Override
    public String toString() {
        return "RoundResult{" + "playerAnimal=" + playerAnimal + ", opponentAnimal=" + opponentAnimal + ", category=" + category + ", winner=" + winner + ", score=" + playerStatValue + " vs " + opponentStatValue + '}';
    }
}

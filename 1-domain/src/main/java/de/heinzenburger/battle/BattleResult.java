package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;

import java.util.Objects;

public final class BattleResult {
    private final RoundWinner winner;
    private final Animal opponentAnimal;
    private final Animal lostAnimal;

    public BattleResult(RoundWinner winner, Animal opponentAnimal, Animal lostAnimal) {
        if (winner == null) {
            throw new IllegalArgumentException("Winner cannot be null");
        }
        if (opponentAnimal == null) {
            throw new IllegalArgumentException("Opponent animal cannot be null");
        }

        this.winner = winner;
        this.opponentAnimal = opponentAnimal;
        this.lostAnimal = lostAnimal;
    }

    public RoundWinner getWinner() {
        return winner;
    }

    public Animal getOpponentAnimal() {
        return opponentAnimal;
    }

    public Animal getLostAnimal() {
        return lostAnimal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleResult that = (BattleResult) o;
        return winner == that.winner &&
                Objects.equals(opponentAnimal, that.opponentAnimal) &&
                Objects.equals(lostAnimal, that.lostAnimal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winner, opponentAnimal, lostAnimal);
    }

    @Override
    public String toString() {
        return "BattleResult{" +
                "winner=" + winner +
                ", opponentAnimal=" + opponentAnimal +
                ", lostAnimal=" + lostAnimal +
                '}';
    }
}

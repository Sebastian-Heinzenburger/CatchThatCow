package de.heinzenburger.battle;

import java.util.Objects;

public final class Score {
    private static final int WINNING_SCORE = 3;
    private final int value;

    public Score() {
        this.value = 0;
    }

    private Score(int value) {
        this.value = value;
    }

    public Score increment() {
        return new Score(value + 1);
    }

    public boolean hasWon() {
        return value >= WINNING_SCORE;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return value == score.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

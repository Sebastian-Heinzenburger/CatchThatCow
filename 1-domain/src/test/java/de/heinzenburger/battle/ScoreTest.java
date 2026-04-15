package de.heinzenburger.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void shouldStartAtZero() {
        Score score = new Score();
        assertEquals(0, score.getValue());
    }

    @Test
    void shouldIncrement() {
        Score score = new Score();
        Score incremented = score.increment();

        assertEquals(1, incremented.getValue());
        assertEquals(0, score.getValue()); // Original should be unchanged (immutable)
    }

    @Test
    void shouldNotHaveWonAtZero() {
        Score score = new Score();
        assertFalse(score.hasWon());
    }

    @Test
    void shouldNotHaveWonBelowThree() {
        Score score = new Score().increment().increment();
        assertFalse(score.hasWon());
    }

    @Test
    void shouldHaveWonAtThree() {
        Score score = new Score().increment().increment().increment();
        assertTrue(score.hasWon());
    }

    @Test
    void shouldHaveWonAboveThree() {
        Score score = new Score().increment().increment().increment().increment();
        assertTrue(score.hasWon());
    }

    @Test
    void shouldBeEqualWhenSameValue() {
        Score score1 = new Score().increment();
        Score score2 = new Score().increment();

        assertEquals(score1, score2);
        assertEquals(score1.hashCode(), score2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValue() {
        Score score1 = new Score().increment();
        Score score2 = new Score().increment().increment();

        assertNotEquals(score1, score2);
    }

    @Test
    void shouldReturnValueAsString() {
        Score score = new Score().increment().increment();
        assertEquals("2", score.toString());
    }
}

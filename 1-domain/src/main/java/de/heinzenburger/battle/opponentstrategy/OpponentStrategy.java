package de.heinzenburger.battle.opponentstrategy;

import de.heinzenburger.shared.StatCategory;

/**
 * Strategy interface for opponent behavior during battles.
 * Allows different AI strategies to be plugged in.
 */
public interface OpponentStrategy {

    /**
     * Selects a stat category for the opponent's attack.
     * @return the selected category
     */
    StatCategory selectCategory();

    /**
     * Resets the strategy state for a new round.
     * Called after each round completes.
     */
    void reset();
}

package de.heinzenburger.session;

/**
 * Represents the current phase of the game state machine.
 */
public enum GamePhase {
    EXPLORING,
    ENCOUNTER_PENDING,  // Animal encountered, player must fight or flee
    IN_BATTLE
}

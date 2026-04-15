package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.session.GamePhase;

/**
 * State interface for the GameSession state machine.
 * Each state knows its valid transitions and operations.
 */
public interface SessionState {

    GamePhase getPhase();

    boolean canMove();

    boolean canStartBattle();

    boolean canFlee();

    SessionState transitionToEncounter(Animal animal);

    SessionState transitionToBattle(Battle battle);

    SessionState transitionToExploring();
}

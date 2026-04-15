package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.session.GamePhase;

import java.util.Optional;

/**
 * State interface for the GameSession state machine.
 * Each state knows its valid transitions and operations.
 * Default implementations return false/throw for operations not supported in most states.
 */
public interface SessionState {

    GamePhase getPhase();

    default boolean canMove() {
        return false;
    }

    default boolean canStartBattle() {
        return false;
    }

    default boolean canFlee() {
        return false;
    }

    default SessionState transitionToEncounter(Animal animal) {
        throw new IllegalStateException("Cannot transition to encounter from " + getPhase());
    }

    default SessionState transitionToBattle(Battle battle) {
        throw new IllegalStateException("Cannot transition to battle from " + getPhase());
    }

    SessionState transitionToExploring();

    default Optional<Animal> getEncounteredAnimal() {
        return Optional.empty();
    }

    default Optional<Battle> getCurrentBattle() {
        return Optional.empty();
    }
}

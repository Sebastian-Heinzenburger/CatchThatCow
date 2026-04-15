package de.heinzenburger.session.state;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.session.GamePhase;

/**
 * Player is exploring the world and can move around.
 * Can transition to ENCOUNTER_PENDING when encountering an animal.
 */
public class ExploringState implements SessionState {

    @Override
    public GamePhase getPhase() {
        return GamePhase.EXPLORING;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public SessionState transitionToEncounter(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null");
        }
        return new EncounterPendingState(animal);
    }

    @Override
    public SessionState transitionToExploring() {
        return this;
    }
}

package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.exception.ApplicationException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;

/**
 * Allows the player to flee from a wild animal encounter.
 * Can only flee from PREY animals, not PREDATORS.
 */
public class FleeEncounterUseCase {

    private final GameSessionManager sessionManager;

    public FleeEncounterUseCase(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Attempts to flee from the current encounter.
     *
     * @throws GameNotStartedException if no active game session
     * @throws InvalidGamePhaseException if not in ENCOUNTER_PENDING phase
     * @throws CannotFleeFromPredatorException if the encountered animal is a PREDATOR
     */
    public void execute() throws GameNotStartedException, InvalidGamePhaseException, CannotFleeFromPredatorException {
        GameSession session = sessionManager.getCurrentSession();

        // Validate phase
        if (session.getPhase() != GamePhase.ENCOUNTER_PENDING) {
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.ENCOUNTER_PENDING);
        }

        Animal encounteredAnimal = session.getEncounteredAnimal();

        // Can only flee from PREY, not PREDATORS
        if (encounteredAnimal.isPredator()) {
            throw new CannotFleeFromPredatorException(encounteredAnimal);
        }

        // Successfully flee - clear the encounter
        session.clearEncounter();
    }

    /**
     * Exception thrown when trying to flee from a predator.
     */
    public static class CannotFleeFromPredatorException extends ApplicationException {
        private final Animal predator;

        public CannotFleeFromPredatorException(Animal predator) {
            super(String.format("Cannot flee from predator: %s", predator.getSpecies().name()));
            this.predator = predator;
        }

        public Animal getPredator() {
            return predator;
        }
    }
}

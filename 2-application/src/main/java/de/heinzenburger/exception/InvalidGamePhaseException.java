package de.heinzenburger.exception;

import de.heinzenburger.session.GamePhase;

/**
 * Thrown when an operation is attempted during an incompatible game phase.
 */
public class InvalidGamePhaseException extends ApplicationException {

    public InvalidGamePhaseException(GamePhase currentPhase, GamePhase requiredPhase) {
        super(String.format("Invalid game phase. Current: %s, Required: %s", currentPhase, requiredPhase));
    }

    public InvalidGamePhaseException(GamePhase currentPhase, String operation) {
        super(String.format("Cannot %s during phase: %s", operation, currentPhase));
    }

    public InvalidGamePhaseException(String message) {
        super(message);
    }

    public InvalidGamePhaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

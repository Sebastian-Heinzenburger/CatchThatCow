package de.heinzenburger.exception;

/**
 * Thrown when an operation requires an active game session but none exists.
 */
public class GameNotStartedException extends ApplicationException {

    public GameNotStartedException() {
        super("No active game session. Start a new game first.");
    }

    public GameNotStartedException(String message) {
        super(message);
    }
}

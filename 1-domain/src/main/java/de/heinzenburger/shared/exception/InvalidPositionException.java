package de.heinzenburger.shared.exception;

import de.heinzenburger.shared.Position;

/**
 * Exception thrown when a position is not valid within a world.
 */
public class InvalidPositionException extends RuntimeException {

    private final Position position;

    public InvalidPositionException(Position position) {
        super("Invalid position: " + position);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}

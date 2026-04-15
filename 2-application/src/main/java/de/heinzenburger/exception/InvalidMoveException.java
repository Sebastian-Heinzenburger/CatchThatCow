package de.heinzenburger.exception;

import de.heinzenburger.shared.Position;
import de.heinzenburger.shared.Direction;

/**
 * Thrown when attempting to move to an invalid position.
 */
public class InvalidMoveException extends ApplicationException {

    public InvalidMoveException(Position targetPosition) {
        super(String.format("Invalid move: position %s is out of bounds", targetPosition));
    }

    public InvalidMoveException(Position currentPosition, Direction direction) {
        super(String.format("Cannot move %s from position %s", direction, currentPosition));
    }

    public InvalidMoveException(String message) {
        super(message);
    }
}

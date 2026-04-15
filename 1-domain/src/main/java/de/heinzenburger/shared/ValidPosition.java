package de.heinzenburger.shared;

import de.heinzenburger.shared.exception.InvalidPositionException;
import de.heinzenburger.world.World;

import java.util.Objects;

/**
 * A position that has been validated against a specific world.
 * This provides compile-time guarantee that the position is valid
 * within the world boundaries.
 */
public final class ValidPosition {
    private final Position position;

    private ValidPosition(Position position) {
        this.position = position;
    }

    /**
     * Creates a ValidPosition after validating against the world.
     *
     * @param position the position to validate
     * @param world    the world to validate against
     * @return a ValidPosition instance
     * @throws InvalidPositionException if position is not valid in the world
     */
    public static ValidPosition of(Position position, World world) {
        validatePositionExistsInWorld(position, world);
        return new ValidPosition(position);
    }

    private static void validatePositionExistsInWorld(Position position, World world) {
        if (position == null) throw new IllegalArgumentException("Position cannot be null");
        if (world == null) throw new IllegalArgumentException("World cannot be null");
        if (!world.isValidPosition(position)) throw new InvalidPositionException(position);
    }

    public Position getPosition() {
        return position;
    }

    public int getX() {
        return position.x();
    }

    public int getY() {
        return position.y();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidPosition that = (ValidPosition) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "ValidPosition{" + position + "}";
    }
}

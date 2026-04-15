package de.heinzenburger.shared;

import java.util.List;
import java.util.Objects;

public final class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceFrom(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public List<Position> adjacentPositions() {
        return List.of(
                neighbour(Direction.NORTH),
                neighbour(Direction.EAST),
                neighbour(Direction.SOUTH),
                neighbour(Direction.WEST)
        );
    }

    public Position neighbour(Direction direction) {
        return switch (direction) {
            case NORTH -> new Position(x, y - 1);
            case EAST -> new Position(x + 1, y);
            case SOUTH -> new Position(x, y + 1);
            case WEST -> new Position(x - 1, y);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

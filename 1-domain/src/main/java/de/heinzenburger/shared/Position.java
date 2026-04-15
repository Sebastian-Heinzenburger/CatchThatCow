package de.heinzenburger.shared;

import java.util.List;

public record Position(int x, int y) {

    public int distanceFrom(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public List<Position> adjacentPositions() {
        return List.of(neighbour(Direction.NORTH), neighbour(Direction.EAST), neighbour(Direction.SOUTH), neighbour(Direction.WEST));
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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

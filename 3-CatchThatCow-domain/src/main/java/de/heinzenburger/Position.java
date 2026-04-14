package de.heinzenburger;

import de.heinzenburger.position.MapUnit;

public class Position {
    static Position ORIGIN = new Position(0, 0);
    private final MapUnit x;
    private final MapUnit y;

    public Position(MapUnit x, MapUnit y) {
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y) {
        this(new MapUnit(x), new MapUnit(y));
    }

    public MapUnit getX() {
        return x;
    }

    public MapUnit getY() {
        return y;
    }

    public Position moveInto(Direction direction) {
        return switch (direction) {
            case NORTH -> this.furtherNorth();
            case SOUTH -> this.furtherSouth();
            case EAST -> this.furtherEast();
            case WEST -> this.furtherWest();
        };
    }

    public Position furtherNorth() {
        return new Position(x, y.decrement());
    }

    public Position furtherSouth() {
        return new Position(x, y.increment());
    }

    public Position furtherEast() {
        return new Position(x.increment(), y);
    }

    public Position furtherWest() {
        return new Position(x.decrement(), y);
    }
}

package de.heinzenburger;

public enum Direction {
    NORTH('N'), EAST('E'), SOUTH('S'), WEST('W');

    final char c;

    Direction(char c) {
        this.c = c;
    }

    static Direction fromChar(char c) {
        for (Direction d : Direction.values()) {
            if (d.c == c) {
                return d;
            }
        }
        throw new IllegalArgumentException("Invalid direction char: " + c);
    }
}

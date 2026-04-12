package de.heinzenburger;

public class Position {
    static Position ORIGIN = new Position(0, 0);
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveInto(Direction direction) {
        switch (direction) {
            case NORTH -> y++;
            case SOUTH -> y--;
            case EAST -> x++;
            case WEST -> x--;
        }
    }

    public Position furtherNorth() {
        return new Position(x, y - 1);
    }

    public Position furtherSouth() {
        return new Position(x, y + 1);
    }

    public Position furtherEast() {
        return new Position(x + 1, y);
    }

    public Position furtherWest() {
        return new Position(x - 1, y);
    }
}

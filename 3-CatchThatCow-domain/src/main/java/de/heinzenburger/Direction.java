package de.heinzenburger;

public enum Direction {
    NORTH("Norden"),
    SOUTH("Süden"),
    EAST("Osten"),
    WEST("Westen");

    private final String displayName;

    Direction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

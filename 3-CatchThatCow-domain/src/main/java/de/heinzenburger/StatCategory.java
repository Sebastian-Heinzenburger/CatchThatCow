package de.heinzenburger;

public enum StatCategory {
    SPEED("Geschwindigkeit"),
    LENGTH("Länge"),
    WEIGHT("Gewicht"),
    STRENGTH("Stärke");

    private final String displayName;

    StatCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

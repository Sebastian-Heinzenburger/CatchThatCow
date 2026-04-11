package de.heinzenburger;

public enum BiomeType {
    START("Startgebiet"),
    DESERT("Wüste"),
    JUNGLE("Dschungel"),
    TUNDRA("Tundra"),
    FOREST("Wald"),
    OCEAN("Ozean"),
    MOUNTAINS("Berge");

    private final String displayName;

    BiomeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

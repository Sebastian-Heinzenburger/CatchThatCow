package de.heinzenburger;

public enum BiomeType {
    DESERT("Wüste"), FOREST("Mischwald"), MOUNTAIN("Gebirge"), SWAMP("Sumpf");

    private final String name;

    BiomeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

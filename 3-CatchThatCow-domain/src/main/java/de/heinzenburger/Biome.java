package de.heinzenburger;

public class Biome {
    private final int level;
    private final BiomeType type;

    public Biome(BiomeType type, int level) {
        this.type = type;
        this.level = level;
    }

    @Override
    public String toString() {
        return type.getName() + " (Level " + level + ")";
    }

    public String toShortString() {
        return type.getName().substring(0, 1) + level;
    }

    public BiomeType getType() {
        return type;
    }
}

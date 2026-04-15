package de.heinzenburger.animal;

import de.heinzenburger.shared.*;

import java.util.EnumMap;
import java.util.Map;

public final class AnimalSpecies {
    private final String name;
    private final int level;
    private final AnimalType type;
    private final AnimalStats baseStats;
    private final BiomeType habitat;

    public AnimalSpecies(String name, int level, AnimalType type, AnimalStats baseStats, BiomeType habitat) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (level < 1 || level > 3) throw new IllegalArgumentException("Level must be between 1 and 3");
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (baseStats == null) throw new IllegalArgumentException("Base stats cannot be null");
        if (habitat == null) throw new IllegalArgumentException("Habitat cannot be null");

        this.name = name;
        this.level = level;
        this.type = type;
        this.baseStats = baseStats;
        this.habitat = habitat;
    }

    private static int varyStatByUptoPercent(int baseStat, double percent, RandomNumberGenerator random) {
        double variation = 1.0 + ((random.nextDouble() * 2 - 1) * percent);
        return (int) Math.round(baseStat * variation);
    }

    public Animal generateAnimalWithSlightStatVariation(RandomNumberGenerator random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        Map<StatCategory, Integer> variedStats = new EnumMap<>(StatCategory.class);

        for (StatCategory category : StatCategory.values()) {
            int baseStat = baseStats.getStat(category);
            int variedStat = varyStatByUptoPercent(baseStat, 0.10, random);
            variedStats.put(category, Math.max(1, variedStat)); // Ensure at least 1
        }

        AnimalStats actualStats = new AnimalStats(variedStats);
        return new Animal(this, actualStats);
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public AnimalType getType() {
        return type;
    }

    public AnimalStats getBaseStats() {
        return baseStats;
    }

    public BiomeType getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return name + " (Level " + level + ", " + type + ", " + habitat + ")";
    }
}

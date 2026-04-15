package de.heinzenburger.animal;

import de.heinzenburger.shared.*;

import java.util.EnumMap;
import java.util.Map;

public record AnimalSpecies(String name, int level, AnimalType type, AnimalStats baseStats, BiomeType habitat) {

    public AnimalSpecies {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (level < 1 || level > 3) throw new IllegalArgumentException("Level must be between 1 and 3");
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (baseStats == null) throw new IllegalArgumentException("Base stats cannot be null");
        if (habitat == null) throw new IllegalArgumentException("Habitat cannot be null");
    }

    private static int varyStatByUpToPercent(int baseStat, double percent, RandomNumberGenerator random) {
        double variation = 1.0 + ((random.nextDouble() * 2 - 1) * percent);
        return (int) Math.round(baseStat * variation);
    }

    public Animal generateAnimalWithSlightStatVariation(RandomNumberGenerator random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        Map<StatCategory, Integer> variedStats = new EnumMap<>(StatCategory.class);

        for (StatCategory category : StatCategory.values()) {
            int baseStat = baseStats.getStat(category);
            int variedStat = varyStatByUpToPercent(baseStat, 0.10, random);
            variedStats.put(category, Math.max(1, variedStat)); // Ensure at least 1
        }

        AnimalStats actualStats = new AnimalStats(variedStats);
        return new Animal(this, actualStats);
    }

    @Override
    public String toString() {
        return name + " (Level " + level + ", " + type + ", " + habitat + ")";
    }
}

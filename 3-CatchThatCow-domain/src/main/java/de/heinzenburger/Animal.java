package de.heinzenburger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Animal {
    private final String species;
    private final String noise;
    private final int level;
    private final AnimalType type;
    private final Map<StatCategory, Integer> stats;

    public Animal(String species, String noise, int level, AnimalType type, Map<StatCategory, Integer> baseStats) {
        this.species = species;
        this.noise = noise;
        this.level = level;
        this.type = type;
        this.stats = applyRandomVariation(baseStats);
    }

    private Map<StatCategory, Integer> applyRandomVariation(Map<StatCategory, Integer> baseStats) {
        Map<StatCategory, Integer> result = new HashMap<>();
        Random random = new Random();

        for (Map.Entry<StatCategory, Integer> entry : baseStats.entrySet()) {
            int baseValue = entry.getValue();
            // ±10% zufällige Variation
            int variation = (int) (baseValue * 0.1 * (random.nextDouble() * 2 - 1));
            result.put(entry.getKey(), baseValue + variation);
        }

        return result;
    }

    public String getSpecies() {
        return species;
    }

    public String getNoise() {
        return noise;
    }

    public int getLevel() {
        return level;
    }

    public AnimalType getType() {
        return type;
    }

    public int getStatValue(StatCategory category) {
        return stats.getOrDefault(category, 0);
    }

    public boolean winsAgainst(Animal other, StatCategory category) {
        return this.getStatValue(category) > other.getStatValue(category);
    }

    @Override
    public String toString() {
        return species + " (Level " + level + ")";
    }
}


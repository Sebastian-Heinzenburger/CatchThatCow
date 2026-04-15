package de.heinzenburger.animal;

import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.StatCategory;

public class Animal {
    private final AnimalSpecies species;
    private final AnimalStats actualStats;

    public Animal(AnimalSpecies species, AnimalStats actualStats) {
        if (species == null) {
            throw new IllegalArgumentException("Species cannot be null");
        }
        if (actualStats == null) {
            throw new IllegalArgumentException("Actual stats cannot be null");
        }

        this.species = species;
        this.actualStats = actualStats;
    }

    public AnimalSpecies getSpecies() {
        return species;
    }

    public AnimalStats getActualStats() {
        return actualStats;
    }

    public int getStat(StatCategory category) {
        return actualStats.getStat(category);
    }

    public int getLevel() {
        return species.getLevel();
    }

    public boolean isPredator() {
        return species.getType() == AnimalType.PREDATOR;
    }

    public boolean isPrey() {
        return species.getType() == AnimalType.PREY;
    }

    @Override
    public String toString() {
        return species.getName();
    }
}

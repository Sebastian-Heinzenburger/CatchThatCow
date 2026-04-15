package de.heinzenburger.animal;

import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.StatCategory;

import java.util.Objects;

public class Animal {
    private final AnimalId id;
    private final AnimalSpecies species;
    private final AnimalStats actualStats;

    public Animal(AnimalSpecies species, AnimalStats actualStats) {
        this(new AnimalId(), species, actualStats);
    }

    public Animal(AnimalId id, AnimalSpecies species, AnimalStats actualStats) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        if (species == null) throw new IllegalArgumentException("Species cannot be null");
        if (actualStats == null) throw new IllegalArgumentException("Actual stats cannot be null");

        this.id = id;
        this.species = species;
        this.actualStats = actualStats;
    }

    public AnimalId getId() {
        return id;
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
        return species.level();
    }

    public boolean isPredator() {
        return species.type() == AnimalType.PREDATOR;
    }

    public boolean isPrey() {
        return species.type() == AnimalType.PREY;
    }

    @Override
    public String toString() {
        return species.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

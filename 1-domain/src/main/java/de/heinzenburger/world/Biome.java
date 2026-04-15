package de.heinzenburger.world;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;

import java.util.Objects;

public class Biome {
    private final Position position;
    private final BiomeType type;
    private final int distanceFromStart;

    public Biome(Position position, BiomeType type, int distanceFromStart) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (distanceFromStart < 0) {
            throw new IllegalArgumentException("Distance from start cannot be negative");
        }

        this.position = position;
        this.type = type;
        this.distanceFromStart = distanceFromStart;
    }

    public Position getPosition() {
        return position;
    }

    public BiomeType getType() {
        return type;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public int getAnimalLevel() {
        return Math.min(3, Math.max(1, distanceFromStart));
    }

    public boolean canContainSpecies(AnimalSpecies species) {
        if (species == null) {
            return false;
        }
        return species.getHabitat() == this.type && species.getLevel() == getAnimalLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biome biome = (Biome) o;
        return Objects.equals(position, biome.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Biome{" +
                "position=" + position +
                ", type=" + type +
                ", level=" + getAnimalLevel() +
                '}';
    }
}

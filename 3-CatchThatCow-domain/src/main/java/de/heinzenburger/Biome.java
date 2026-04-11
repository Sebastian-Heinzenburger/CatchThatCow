package de.heinzenburger;

import java.util.*;

public class Biome {
    private final BiomeType type;
    private final Position position;
    private final int level;
    private final List<Animal> possibleAnimals;

    public Biome(BiomeType type, Position position, List<Animal> possibleAnimals) {
        this.type = type;
        this.position = position;
        this.level = position.distanceFromCenter();
        this.possibleAnimals = possibleAnimals;
    }

    public BiomeType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public int getLevel() {
        return level;
    }

    public Animal spawnRandomAnimal() {
        if (possibleAnimals.isEmpty()) {
            throw new IllegalStateException("Keine Tiere in diesem Biom verfügbar!");
        }

        Random random = new Random();
        int index = random.nextInt(possibleAnimals.size());

        // Erstelle eine Kopie des Tieres (neue Instanz mit neuen zufälligen Stats)
        Animal template = possibleAnimals.get(index);
        Map<StatCategory, Integer> baseStats = new HashMap<>();
        for (StatCategory category : StatCategory.values()) {
            baseStats.put(category, template.getStatValue(category));
        }

        return new Animal(
            template.getSpecies(),
            template.getNoise(),
            template.getLevel(),
            template.getType(),
            baseStats
        );
    }
}

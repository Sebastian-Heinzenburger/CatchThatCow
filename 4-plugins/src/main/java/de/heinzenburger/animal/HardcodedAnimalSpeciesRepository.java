package de.heinzenburger.animal;

import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.StatCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Hardcoded implementation of AnimalSpeciesRepository containing all game species.
 * Each biome has 2 species per level (1 predator, 1 prey) for levels 1-3.
 * Total: 6 biomes * 3 levels * 2 types = 36 species.
 */
public final class HardcodedAnimalSpeciesRepository implements AnimalSpeciesRepository {

    private final List<AnimalSpecies> allSpecies;

    public HardcodedAnimalSpeciesRepository() {
        this.allSpecies = createAllSpecies();
    }

    @Override
    public List<AnimalSpecies> findAll() {
        return new ArrayList<>(allSpecies);
    }

    @Override
    public List<AnimalSpecies> findByLevel(int level) {
        return allSpecies.stream()
                .filter(species -> species.level() == level)
                .toList();
    }

    @Override
    public List<AnimalSpecies> findByHabitat(BiomeType biomeType) {
        return allSpecies.stream()
                .filter(species -> species.habitat() == biomeType)
                .toList();
    }

    private List<AnimalSpecies> createAllSpecies() {
        List<AnimalSpecies> species = new ArrayList<>();

        // GRASSLAND species
        species.add(createSpecies("Rabbit", 1, AnimalType.PREY, BiomeType.GRASSLAND, 85, 30, 3, 8, 30));
        species.add(createSpecies("Fox", 1, AnimalType.PREDATOR, BiomeType.GRASSLAND, 70, 60, 8, 12, 6));
        species.add(createSpecies("Deer", 2, AnimalType.PREY, BiomeType.GRASSLAND, 75, 150, 80, 15, 2));
        species.add(createSpecies("Wolf", 2, AnimalType.PREDATOR, BiomeType.GRASSLAND, 65, 140, 45, 13, 6));
        species.add(createSpecies("Bison", 3, AnimalType.PREY, BiomeType.GRASSLAND, 55, 300, 900, 20, 1));
        species.add(createSpecies("Lion", 3, AnimalType.PREDATOR, BiomeType.GRASSLAND, 80, 200, 190, 15, 4));

        // DESERT species
        species.add(createSpecies("Jerboa", 1, AnimalType.PREY, BiomeType.DESERT, 80, 15, 1, 6, 12));
        species.add(createSpecies("Scorpion", 1, AnimalType.PREDATOR, BiomeType.DESERT, 25, 10, 1, 5, 50));
        species.add(createSpecies("Camel", 2, AnimalType.PREY, BiomeType.DESERT, 40, 220, 500, 40, 1));
        species.add(createSpecies("Coyote", 2, AnimalType.PREDATOR, BiomeType.DESERT, 65, 100, 15, 14, 6));
        species.add(createSpecies("Ostrich", 3, AnimalType.PREY, BiomeType.DESERT, 95, 230, 130, 45, 15));
        species.add(createSpecies("Hyena", 3, AnimalType.PREDATOR, BiomeType.DESERT, 60, 150, 55, 20, 3));

        // TUNDRA species
        species.add(createSpecies("Lemming", 1, AnimalType.PREY, BiomeType.TUNDRA, 30, 12, 1, 2, 40));
        species.add(createSpecies("Ermine", 1, AnimalType.PREDATOR, BiomeType.TUNDRA, 55, 30, 1, 7, 8));
        species.add(createSpecies("Reindeer", 2, AnimalType.PREY, BiomeType.TUNDRA, 60, 180, 180, 15, 1));
        species.add(createSpecies("Wolverine", 2, AnimalType.PREDATOR, BiomeType.TUNDRA, 50, 80, 20, 13, 3));
        species.add(createSpecies("Muskox", 3, AnimalType.PREY, BiomeType.TUNDRA, 40, 240, 400, 20, 1));
        species.add(createSpecies("Polar Bear", 3, AnimalType.PREDATOR, BiomeType.TUNDRA, 45, 250, 500, 25, 2));

        // JUNGLE species
        species.add(createSpecies("Toucan", 1, AnimalType.PREY, BiomeType.JUNGLE, 45, 50, 1, 20, 4));
        species.add(createSpecies("Snake", 1, AnimalType.PREDATOR, BiomeType.JUNGLE, 20, 200, 5, 20, 20));
        species.add(createSpecies("Tapir", 2, AnimalType.PREY, BiomeType.JUNGLE, 35, 200, 300, 30, 1));
        species.add(createSpecies("Jaguar", 2, AnimalType.PREDATOR, BiomeType.JUNGLE, 80, 170, 100, 15, 3));
        species.add(createSpecies("Gorilla", 3, AnimalType.PREY, BiomeType.JUNGLE, 25, 170, 180, 40, 1));
        species.add(createSpecies("Tiger", 3, AnimalType.PREDATOR, BiomeType.JUNGLE, 70, 280, 250, 18, 3));

        // FOREST species
        species.add(createSpecies("Squirrel", 1, AnimalType.PREY, BiomeType.FOREST, 35, 25, 1, 12, 6));
        species.add(createSpecies("Weasel", 1, AnimalType.PREDATOR, BiomeType.FOREST, 55, 30, 1, 10, 6));
        species.add(createSpecies("Boar", 2, AnimalType.PREY, BiomeType.FOREST, 45, 150, 100, 20, 6));
        species.add(createSpecies("Lynx", 2, AnimalType.PREDATOR, BiomeType.FOREST, 60, 100, 25, 15, 3));
        species.add(createSpecies("Moose", 3, AnimalType.PREY, BiomeType.FOREST, 55, 280, 600, 20, 1));
        species.add(createSpecies("Bear", 3, AnimalType.PREDATOR, BiomeType.FOREST, 45, 200, 350, 25, 2));

        // OCEAN species
        species.add(createSpecies("Herring", 1, AnimalType.PREY, BiomeType.OCEAN, 40, 30, 1, 15, 100));
        species.add(createSpecies("Barracuda", 1, AnimalType.PREDATOR, BiomeType.OCEAN, 75, 150, 25, 14, 30));
        species.add(createSpecies("Dolphin", 2, AnimalType.PREY, BiomeType.OCEAN, 55, 300, 200, 45, 1));
        species.add(createSpecies("Shark", 2, AnimalType.PREDATOR, BiomeType.OCEAN, 60, 400, 400, 30, 5));
        species.add(createSpecies("Blue Whale", 3, AnimalType.PREY, BiomeType.OCEAN, 30, 2500, 50000, 80, 1));
        species.add(createSpecies("Orca", 3, AnimalType.PREDATOR, BiomeType.OCEAN, 65, 800, 6000, 50, 1));

        return species;
    }

    private AnimalSpecies createSpecies(String name, int level, AnimalType type, BiomeType habitat,
                                        int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.LIFESPAN, lifespan);
        stats.put(StatCategory.OFFSPRING, offspring);
        return new AnimalSpecies(name, level, type, new AnimalStats(stats), habitat);
    }
}

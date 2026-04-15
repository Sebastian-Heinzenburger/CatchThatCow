package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.player.Inventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.shared.*;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.*;

/**
 * Factory for creating test data objects used across multiple test classes.
 * Reduces duplication and provides consistent test data creation.
 */
public class TestDataFactory {

    private static final long DEFAULT_SEED = 42L;
    private static final RandomNumberGenerator TEST_RANDOM = new RandomTestAdapter(DEFAULT_SEED);

    /**
     * Creates a player with a specified number of animals at position (0,0).
     */
    public static Player createPlayerWithAnimals(int animalCount) {
        return createPlayerAtPosition(new Position(0, 0), animalCount);
    }

    /**
     * Creates a player at a specific position with a specified number of animals.
     */
    public static Player createPlayerAtPosition(Position position, int animalCount) {
        Inventory inventory = new Inventory(TEST_RANDOM);
        Player player = new Player(inventory, position);

        for (int i = 0; i < animalCount; i++) {
            player.addAnimal(createAnimal("TestAnimal" + i, 1, AnimalType.PREY));
        }

        return player;
    }

    /**
     * Creates a single animal with specified properties.
     */
    public static Animal createAnimal(String name, int level, AnimalType type) {
        Map<StatCategory, Integer> stats = createStatsMap(60, 60, 60, 60, 60);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies(name, level, type, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    /**
     * Creates a predator animal with specified name and level.
     */
    public static Animal createPredator(String name, int level) {
        Map<StatCategory, Integer> stats = createStatsMap(75, 75, 75, 75, 75);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies(name, level, AnimalType.PREDATOR, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    /**
     * Creates a prey animal with specified name and level.
     */
    public static Animal createPrey(String name, int level) {
        Map<StatCategory, Integer> stats = createStatsMap(50, 50, 50, 50, 50);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies(name, level, AnimalType.PREY, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    /**
     * Creates an animal with custom stats.
     */
    public static Animal createAnimalWithStats(String name, int level, AnimalType type, int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = createStatsMap(speed, length, weight, lifespan, offspring);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies(name, level, type, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    /**
     * Creates a simple test world with specified size.
     */
    public static World createTestWorld(int size) {
        Position startPosition = new Position(0, 0);
        Map<Position, Biome> biomeMap = new HashMap<>();

        // Create a simple grid of grassland biomes
        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                Position pos = new Position(x, y);
                Biome biome = new Biome(pos, BiomeType.GRASSLAND, 1);
                biomeMap.put(pos, biome);
            }
        }

        int worldSize = size * 2 + 1;
        return new World(worldSize, startPosition, biomeMap);
    }

    /**
     * Creates a list of test animals.
     */
    public static List<Animal> createAnimalList(int count) {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            animals.add(createAnimal("Animal" + i, 1, AnimalType.PREY));
        }
        return animals;
    }

    /**
     * Creates a list of animal species for testing.
     */
    public static List<AnimalSpecies> createAnimalSpeciesList(int count, int level) {
        List<AnimalSpecies> species = new ArrayList<>();
        BiomeType[] biomeTypes = BiomeType.values();

        for (int i = 0; i < count; i++) {
            // Cycle through biome types to ensure coverage
            BiomeType biomeType = biomeTypes[i % biomeTypes.length];
            Map<StatCategory, Integer> stats = createStatsMap(60, 60, 60, 60, 60);
            AnimalStats animalStats = new AnimalStats(stats);
            AnimalType type = (i % 2 == 0) ? AnimalType.PREY : AnimalType.PREDATOR;
            species.add(new AnimalSpecies("Species" + i, level, type, animalStats, biomeType));
        }
        return species;
    }

    /**
     * Returns a seeded random number generator for deterministic tests.
     */
    public static RandomNumberGenerator getTestRandom() {
        return TEST_RANDOM;
    }

    /**
     * Returns a new seeded random number generator with a custom seed.
     */
    public static RandomNumberGenerator getTestRandom(long seed) {
        return new RandomTestAdapter(seed);
    }

    /**
     * Creates a stats map with specified values.
     */
    private static Map<StatCategory, Integer> createStatsMap(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.LIFESPAN, lifespan);
        stats.put(StatCategory.OFFSPRING, offspring);
        return stats;
    }
}

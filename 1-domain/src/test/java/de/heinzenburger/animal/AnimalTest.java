package de.heinzenburger.animal;

import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.StatCategory;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void shouldCreateAnimal() {
        AnimalSpecies species = createTestSpecies();
        AnimalStats stats = createStats(10, 20, 30, 40, 50);
        Animal animal = new Animal(species, stats);

        assertEquals(species, animal.getSpecies());
        assertEquals(stats, animal.getActualStats());
    }

    @Test
    void shouldGetStatFromActualStats() {
        AnimalSpecies species = createTestSpecies();
        AnimalStats stats = createStats(15, 25, 35, 45, 55);
        Animal animal = new Animal(species, stats);

        assertEquals(15, animal.getStat(StatCategory.SPEED));
        assertEquals(25, animal.getStat(StatCategory.LENGTH));
    }

    @Test
    void shouldGetLevelFromSpecies() {
        AnimalSpecies species = createTestSpecies();
        AnimalStats stats = createStats(10, 20, 30, 40, 50);
        Animal animal = new Animal(species, stats);

        assertEquals(2, animal.getLevel());
    }

    @Test
    void shouldIdentifyPredator() {
        AnimalStats baseStats = createStats(10, 20, 30, 40, 50);
        AnimalSpecies predatorSpecies = new AnimalSpecies("Lion", 2, AnimalType.PREDATOR, baseStats, BiomeType.GRASSLAND);
        Animal predator = new Animal(predatorSpecies, baseStats);

        assertTrue(predator.isPredator());
        assertFalse(predator.isPrey());
    }

    @Test
    void shouldIdentifyPrey() {
        AnimalStats baseStats = createStats(10, 20, 30, 40, 50);
        AnimalSpecies preySpecies = new AnimalSpecies("Rabbit", 1, AnimalType.PREY, baseStats, BiomeType.FOREST);
        Animal prey = new Animal(preySpecies, baseStats);

        assertTrue(prey.isPrey());
        assertFalse(prey.isPredator());
    }

    private AnimalSpecies createTestSpecies() {
        AnimalStats baseStats = createStats(10, 20, 30, 40, 50);
        return new AnimalSpecies("Test Animal", 2, AnimalType.PREDATOR, baseStats, BiomeType.DESERT);
    }

    private AnimalStats createStats(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.LIFESPAN, lifespan);
        stats.put(StatCategory.OFFSPRING, offspring);
        return new AnimalStats(stats);
    }
}

package de.heinzenburger.services;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.shared.*;
import de.heinzenburger.world.Biome;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnimalEncounterServiceTest {

    @Test
    void shouldEncounterAnimalInBiome() {
        AnimalEncounterService service = new AnimalEncounterService(new RandomTestAdapter(42));
        Biome biome = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        List<AnimalSpecies> species = List.of(
                createSpecies("Cobra", 2, BiomeType.DESERT),
                createSpecies("Camel", 2, BiomeType.DESERT)
        );

        Animal animal = service.encounterAnimal(biome, species);

        assertNotNull(animal);
        assertEquals(BiomeType.DESERT, animal.getSpecies().habitat());
        assertEquals(2, animal.getLevel());
    }

    @Test
    void shouldFilterIncompatibleSpecies() {
        AnimalEncounterService service = new AnimalEncounterService(new RandomTestAdapter(42));
        Biome desert = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        List<AnimalSpecies> species = List.of(
                createSpecies("Bear", 2, BiomeType.FOREST), // Incompatible habitat
                createSpecies("Cobra", 1, BiomeType.DESERT), // Incompatible level
                createSpecies("Scorpion", 2, BiomeType.DESERT) // Compatible
        );

        Animal animal = service.encounterAnimal(desert, species);

        assertEquals("Scorpion", animal.getSpecies().name());
    }

    @Test
    void shouldThrowExceptionWhenNoCompatibleSpecies() {
        AnimalEncounterService service = new AnimalEncounterService(new RandomTestAdapter());
        Biome desert = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        List<AnimalSpecies> species = List.of(
                createSpecies("Bear", 2, BiomeType.FOREST)
        );

        assertThrows(IllegalStateException.class, () -> service.encounterAnimal(desert, species));
    }

    @Test
    void shouldGenerateAnimalWithVariedStats() {
        AnimalEncounterService service = new AnimalEncounterService(new RandomTestAdapter(42));
        Biome biome = new Biome(new Position(0, 0), BiomeType.DESERT, 1);
        AnimalSpecies species = createSpecies("Test", 1, BiomeType.DESERT);

        Animal animal1 = service.encounterAnimal(biome, List.of(species));
        Animal animal2 = service.encounterAnimal(biome, List.of(species));

        // Should be different instances (reference inequality)
        assertNotSame(animal1, animal2);
    }

    private AnimalSpecies createSpecies(String name, int level, BiomeType habitat) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 100);
        stats.put(StatCategory.LENGTH, 100);
        stats.put(StatCategory.WEIGHT, 100);
        stats.put(StatCategory.LIFESPAN, 100);
        stats.put(StatCategory.OFFSPRING, 100);

        AnimalStats animalStats = new AnimalStats(stats);
        return new AnimalSpecies(name, level, AnimalType.PREY, animalStats, habitat);
    }
}

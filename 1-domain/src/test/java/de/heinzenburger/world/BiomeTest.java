package de.heinzenburger.world;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BiomeTest {

    @Test
    void shouldCreateBiome() {
        Position position = new Position(5, 5);
        Biome biome = new Biome(position, BiomeType.DESERT, 2);

        assertEquals(position, biome.position());
        assertEquals(BiomeType.DESERT, biome.type());
        assertEquals(2, biome.distanceFromStart());
    }

    @Test
    void shouldCalculateAnimalLevel() {
        Biome level1 = new Biome(new Position(0, 0), BiomeType.DESERT, 1);
        Biome level2 = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        Biome level3 = new Biome(new Position(0, 0), BiomeType.DESERT, 3);
        Biome level4Plus = new Biome(new Position(0, 0), BiomeType.DESERT, 5);

        assertEquals(1, level1.getAnimalLevel());
        assertEquals(2, level2.getAnimalLevel());
        assertEquals(3, level3.getAnimalLevel());
        assertEquals(3, level4Plus.getAnimalLevel()); // Capped at 3
    }

    @Test
    void shouldAcceptCompatibleSpecies() {
        Biome desert = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        AnimalSpecies desertSpecies = createSpecies("Cobra", 2, BiomeType.DESERT);

        assertTrue(desert.canContainSpecies(desertSpecies));
    }

    @Test
    void shouldRejectIncompatibleHabitat() {
        Biome desert = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        AnimalSpecies forestSpecies = createSpecies("Bear", 2, BiomeType.FOREST);

        assertFalse(desert.canContainSpecies(forestSpecies));
    }

    @Test
    void shouldRejectIncompatibleLevel() {
        Biome desert = new Biome(new Position(0, 0), BiomeType.DESERT, 2);
        AnimalSpecies level1Species = createSpecies("Lizard", 1, BiomeType.DESERT);

        assertFalse(desert.canContainSpecies(level1Species));
    }

    @Test
    void shouldBeEqualWhenPositionsMatch() {
        Position position = new Position(5, 5);
        Biome biome1 = new Biome(position, BiomeType.DESERT, 2);
        Biome biome2 = new Biome(position, BiomeType.FOREST, 2);

        assertEquals(biome1, biome2);
        assertEquals(biome1.hashCode(), biome2.hashCode());
    }

    private AnimalSpecies createSpecies(String name, int level, BiomeType habitat) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 10);
        stats.put(StatCategory.LENGTH, 20);
        stats.put(StatCategory.WEIGHT, 30);
        stats.put(StatCategory.LIFESPAN, 40);
        stats.put(StatCategory.OFFSPRING, 50);

        AnimalStats animalStats = new AnimalStats(stats);
        return new AnimalSpecies(name, level, AnimalType.PREY, animalStats, habitat);
    }
}

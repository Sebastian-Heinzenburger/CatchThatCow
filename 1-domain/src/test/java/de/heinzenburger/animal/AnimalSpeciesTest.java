package de.heinzenburger.animal;

import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.RandomTestAdapter;
import de.heinzenburger.shared.StatCategory;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalSpeciesTest {

    @Test
    void shouldCreateAnimalSpecies() {
        AnimalStats baseStats = createStats(10, 20, 30, 40, 50);
        AnimalSpecies species = new AnimalSpecies("Cobra", 2, AnimalType.PREDATOR, baseStats, BiomeType.DESERT);

        assertEquals("Cobra", species.getName());
        assertEquals(2, species.getLevel());
        assertEquals(AnimalType.PREDATOR, species.getType());
        assertEquals(baseStats, species.getBaseStats());
        assertEquals(BiomeType.DESERT, species.getHabitat());
    }

    @Test
    void shouldThrowExceptionForInvalidLevel() {
        AnimalStats baseStats = createStats(10, 20, 30, 40, 50);
        assertThrows(IllegalArgumentException.class,
                () -> new AnimalSpecies("Cobra", 0, AnimalType.PREDATOR, baseStats, BiomeType.DESERT));
        assertThrows(IllegalArgumentException.class,
                () -> new AnimalSpecies("Cobra", 4, AnimalType.PREDATOR, baseStats, BiomeType.DESERT));
    }

    @Test
    void shouldGenerateAnimalWithSlightStatVariationWithVariedStats() {
        AnimalStats baseStats = createStats(100, 100, 100, 100, 100);
        AnimalSpecies species = new AnimalSpecies("Test", 1, AnimalType.PREY, baseStats, BiomeType.FOREST);

        Animal animal = species.generateAnimalWithSlightStatVariation(new RandomTestAdapter(42));

        assertNotNull(animal);
        assertEquals(species, animal.getSpecies());

        // Stats should vary by ±10% from base (90-110 for base of 100)
        for (StatCategory category : StatCategory.values()) {
            int stat = animal.getStat(category);
            assertTrue(stat >= 90 && stat <= 110,
                    "Stat " + category + " value " + stat + " should be between 90 and 110");
        }
    }

    @Test
    void shouldGenerateDifferentAnimalsWithDifferentSeeds() {
        AnimalStats baseStats = createStats(100, 100, 100, 100, 100);
        AnimalSpecies species = new AnimalSpecies("Test", 1, AnimalType.PREY, baseStats, BiomeType.FOREST);

        Animal animal1 = species.generateAnimalWithSlightStatVariation(new RandomTestAdapter(1));
        Animal animal2 = species.generateAnimalWithSlightStatVariation(new RandomTestAdapter(2));

        // Animals are different instances (reference inequality)
        assertNotSame(animal1, animal2);

        // At least one stat should be different (with high probability)
        boolean hasDifference = false;
        for (StatCategory category : StatCategory.values()) {
            if (animal1.getStat(category) != animal2.getStat(category)) {
                hasDifference = true;
                break;
            }
        }
        assertTrue(hasDifference, "Animals should have at least one different stat");
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

package de.heinzenburger.shared;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnimalStatsTest {

    @Test
    void shouldCreateAnimalStatsWithAllCategories() {
        Map<StatCategory, Integer> stats = createFullStats(10, 20, 30, 40, 50);
        AnimalStats animalStats = new AnimalStats(stats);

        assertEquals(10, animalStats.getStat(StatCategory.SPEED));
        assertEquals(20, animalStats.getStat(StatCategory.LENGTH));
        assertEquals(30, animalStats.getStat(StatCategory.WEIGHT));
        assertEquals(40, animalStats.getStat(StatCategory.LIFESPAN));
        assertEquals(50, animalStats.getStat(StatCategory.OFFSPRING));
    }

    @Test
    void shouldThrowExceptionWhenStatsAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new AnimalStats(null));
    }

    @Test
    void shouldThrowExceptionWhenStatsAreEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new AnimalStats(new EnumMap<>(StatCategory.class)));
    }

    @Test
    void shouldThrowExceptionWhenMissingStatCategory() {
        Map<StatCategory, Integer> incompleteStats = new EnumMap<>(StatCategory.class);
        incompleteStats.put(StatCategory.SPEED, 10);
        incompleteStats.put(StatCategory.LENGTH, 20);
        // Missing other categories
        assertThrows(IllegalArgumentException.class, () -> new AnimalStats(incompleteStats));
    }

    @Test
    void shouldThrowExceptionWhenStatIsNegative() {
        Map<StatCategory, Integer> stats = createFullStats(-1, 20, 30, 40, 50);
        assertThrows(IllegalArgumentException.class, () -> new AnimalStats(stats));
    }

    @Test
    void shouldCompareStats() {
        Map<StatCategory, Integer> stats1 = createFullStats(10, 20, 30, 40, 50);
        Map<StatCategory, Integer> stats2 = createFullStats(15, 20, 30, 40, 50);

        AnimalStats animalStats1 = new AnimalStats(stats1);
        AnimalStats animalStats2 = new AnimalStats(stats2);

        assertTrue(animalStats1.compareTo(animalStats2, StatCategory.SPEED) < 0);
        assertEquals(0, animalStats1.compareTo(animalStats2, StatCategory.LENGTH));
        assertTrue(animalStats2.compareTo(animalStats1, StatCategory.SPEED) > 0);
    }

    @Test
    void shouldBeEqualWhenAllStatsMatch() {
        Map<StatCategory, Integer> stats = createFullStats(10, 20, 30, 40, 50);
        AnimalStats stats1 = new AnimalStats(stats);
        AnimalStats stats2 = new AnimalStats(stats);

        assertEquals(stats1, stats2);
        assertEquals(stats1.hashCode(), stats2.hashCode());
    }

    @Test
    void shouldBeImmutable() {
        Map<StatCategory, Integer> stats = createFullStats(10, 20, 30, 40, 50);
        AnimalStats animalStats = new AnimalStats(stats);

        // Modify original map
        stats.put(StatCategory.SPEED, 999);

        // Should not affect the AnimalStats
        assertEquals(10, animalStats.getStat(StatCategory.SPEED));
    }

    private Map<StatCategory, Integer> createFullStats(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.LIFESPAN, lifespan);
        stats.put(StatCategory.OFFSPRING, offspring);
        return stats;
    }
}

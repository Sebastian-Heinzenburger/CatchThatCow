package de.heinzenburger.services;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.RandomTestAdapter;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldGeneratorTest {

    @Test
    void shouldGenerateWorldWithCorrectSize() {
        WorldGenerator generator = new WorldGenerator(new RandomTestAdapter(42));
        World world = generator.generateWorld(2);

        assertEquals(5, world.getSize()); // (2*2+1) = 5
        assertEquals(25, world.getAllBiomes().size()); // 5x5 grid
    }

    @Test
    void shouldPlaceStartAtCenter() {
        WorldGenerator generator = new WorldGenerator(new RandomTestAdapter(42));
        World world = generator.generateWorld(3);

        Position startPos = world.getStartPosition();
        assertEquals(new Position(3, 3), startPos);
    }

    @Test
    void shouldCalculateCorrectDistances() {
        WorldGenerator generator = new WorldGenerator(new RandomTestAdapter(42));
        World world = generator.generateWorld(2);

        Position center = new Position(2, 2);
        Biome centerBiome = world.getBiomeAt(center);

        assertEquals(0, centerBiome.distanceFromStart());

        Position adjacent = new Position(2, 1);
        Biome adjacentBiome = world.getBiomeAt(adjacent);
        assertEquals(1, adjacentBiome.distanceFromStart());
    }

    @Test
    void shouldAssignRandomBiomeTypes() {
        WorldGenerator generator = new WorldGenerator(new RandomTestAdapter(42));
        World world = generator.generateWorld(2);

        boolean hasVariety = false;
        BiomeType firstType = null;

        for (Biome biome : world.getAllBiomes().values()) {
            if (firstType == null) {
                firstType = biome.type();
            } else if (biome.type() != firstType) {
                hasVariety = true;
                break;
            }
        }

        assertTrue(hasVariety, "World should have variety in biome types");
    }

    @Test
    void shouldThrowExceptionForInvalidN() {
        WorldGenerator generator = new WorldGenerator(new RandomTestAdapter());
        assertThrows(IllegalArgumentException.class, () -> generator.generateWorld(0));
        assertThrows(IllegalArgumentException.class, () -> generator.generateWorld(-1));
    }
}

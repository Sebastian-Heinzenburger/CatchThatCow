package de.heinzenburger.world;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    @Test
    void shouldCreateWorld() {
        Position startPos = new Position(1, 1);
        Map<Position, Biome> biomes = createTestBiomes();

        World world = new World(3, startPos, biomes);

        assertEquals(3, world.getSize());
        assertEquals(startPos, world.getStartPosition());
        assertFalse(world.getAllBiomes().isEmpty());
    }

    @Test
    void shouldGetBiomeAtPosition() {
        Position startPos = new Position(1, 1);
        Map<Position, Biome> biomes = createTestBiomes();

        World world = new World(3, startPos, biomes);

        Position testPos = new Position(0, 0);
        Biome biome = world.getBiomeAt(testPos);

        assertNotNull(biome);
        assertEquals(testPos, biome.getPosition());
    }

    @Test
    void shouldGetAdjacentBiomes() {
        Position startPos = new Position(1, 1);
        Map<Position, Biome> biomes = createTestBiomes();

        World world = new World(3, startPos, biomes);

        Map<Direction, Biome> adjacent = world.getAdjacentBiomes(startPos);

        assertTrue(adjacent.containsKey(Direction.NORTH));
        assertTrue(adjacent.containsKey(Direction.EAST));
        assertTrue(adjacent.containsKey(Direction.SOUTH));
        assertTrue(adjacent.containsKey(Direction.WEST));
        assertEquals(4, adjacent.size());
    }

    @Test
    void shouldHandleEdgeBiomesWithFewerAdjacent() {
        Position startPos = new Position(1, 1);
        Map<Position, Biome> biomes = createTestBiomes();

        World world = new World(3, startPos, biomes);

        Position corner = new Position(0, 0);
        Map<Direction, Biome> adjacent = world.getAdjacentBiomes(corner);

        // Corner should only have 2 adjacent biomes
        assertEquals(2, adjacent.size());
        assertTrue(adjacent.containsKey(Direction.EAST));
        assertTrue(adjacent.containsKey(Direction.SOUTH));
    }

    @Test
    void shouldValidatePosition() {
        Position startPos = new Position(1, 1);
        Map<Position, Biome> biomes = createTestBiomes();

        World world = new World(3, startPos, biomes);

        assertTrue(world.isValidPosition(new Position(0, 0)));
        assertTrue(world.isValidPosition(new Position(1, 1)));
        assertFalse(world.isValidPosition(new Position(10, 10)));
    }

    private Map<Position, Biome> createTestBiomes() {
        Map<Position, Biome> biomes = new HashMap<>();
        // Create a 3x3 grid
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Position pos = new Position(x, y);
                Position center = new Position(1, 1);
                int distance = center.distanceFrom(pos);
                Biome biome = new Biome(pos, BiomeType.GRASSLAND, distance);
                biomes.put(pos, biome);
            }
        }
        return biomes;
    }
}

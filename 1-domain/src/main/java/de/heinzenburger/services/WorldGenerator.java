package de.heinzenburger.services;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.*;

public class WorldGenerator {
    private final RandomNumberGenerator random;

    public WorldGenerator(RandomNumberGenerator random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        this.random = random;
    }

    public World generateWorld(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        int gridSize = 2 * n + 1;
        Position startPosition = new Position(n, n); // Center of the grid
        Map<Position, Biome> biomes = new HashMap<>();

        BiomeType[] biomeTypes = BiomeType.values();

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Position position = new Position(x, y);
                int distance = startPosition.distanceFrom(position);

                // Randomly select a biome type
                BiomeType type = biomeTypes[random.nextInt(biomeTypes.length)];

                Biome biome = new Biome(position, type, distance);
                biomes.put(position, biome);
            }
        }

        return new World(gridSize, startPosition, biomes);
    }
}

package de.heinzenburger.ui.dto;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Presentation DTO for displaying map data in the UI.
 * Decouples the view layer from domain entities.
 */
public record MapData(
        Map<Position, BiomeData> biomes,
        Position playerPosition,
        int minX, int maxX, int minY, int maxY
) {
    public record BiomeData(BiomeType type, int animalLevel) {
        public static BiomeData from(Biome biome) {
            return new BiomeData(biome.type(), biome.getAnimalLevel());
        }
    }

    public static MapData from(World world, Position playerPosition) {
        Map<Position, BiomeData> biomeData = new HashMap<>();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (var entry : world.getAllBiomes().entrySet()) {
            Position pos = entry.getKey();
            biomeData.put(pos, BiomeData.from(entry.getValue()));
            minX = Math.min(minX, pos.x());
            maxX = Math.max(maxX, pos.x());
            minY = Math.min(minY, pos.y());
            maxY = Math.max(maxY, pos.y());
        }

        return new MapData(biomeData, playerPosition, minX, maxX, minY, maxY);
    }

    public BiomeData getBiomeAt(Position position) {
        return biomes.get(position);
    }
}

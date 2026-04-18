package de.heinzenburger.ui.view;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;

import java.util.Map;

/**
 * Renders the world map as ASCII art.
 */
public class WorldRenderer {

    public String render(World world, Position playerPosition) {
        Map<Position, Biome> allBiomes = world.getAllBiomes();

        // Find bounds
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Position pos : allBiomes.keySet()) {
            minX = Math.min(minX, pos.x());
            maxX = Math.max(maxX, pos.x());
            minY = Math.min(minY, pos.y());
            maxY = Math.max(maxY, pos.y());
        }

        StringBuilder sb = new StringBuilder();

        // Header with column numbers
        sb.append("     ");
        for (int x = minX; x <= maxX; x++) {
            sb.append(String.format("%2d", x));
        }
        sb.append("\n");

        // Top border
        sb.append("   ┌");
        sb.append("──".repeat(maxX - minX + 1));
        sb.append("┐\n");

        // Grid with biomes
        for (int y = minY; y <= maxY; y++) {
            sb.append(String.format("%2d │", y));
            for (int x = minX; x <= maxX; x++) {
                Position pos = new Position(x, y);
                if (pos.equals(playerPosition)) {
                    sb.append(" @");
                } else {
                    Biome biome = allBiomes.get(pos);
                    if (biome != null) {
                        sb.append(" ").append(getBiomeChar(biome.type()));
                    } else {
                        sb.append(" .");
                    }
                }
            }
            sb.append("│\n");
        }

        // Bottom border
        sb.append("   └");
        sb.append("──".repeat(maxX - minX + 1));
        sb.append("┘\n");

        // Legend
        Biome currentBiome = allBiomes.get(playerPosition);
        if (currentBiome != null) {
            sb.append("\nPosition: ").append(playerPosition)
              .append(" - ").append(currentBiome.type())
              .append(" (Level ").append(currentBiome.getAnimalLevel()).append(")\n");
        }

        sb.append("\nLegend: G=Grassland D=Desert T=Tundra J=Jungle F=Forest O=Ocean @=You\n");

        return sb.toString();
    }

    private char getBiomeChar(BiomeType type) {
        return switch (type) {
            case GRASSLAND -> 'G';
            case DESERT -> 'D';
            case TUNDRA -> 'T';
            case JUNGLE -> 'J';
            case FOREST -> 'F';
            case OCEAN -> 'O';
        };
    }
}

package de.heinzenburger.ui.view;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.ui.dto.MapData;

import static de.heinzenburger.ui.view.RenderConstants.*;

/**
 * Renders the world map as ASCII art.
 */
public class WorldRenderer {

    public String render(MapData map) {
        StringBuilder sb = new StringBuilder();

        renderColumnHeader(sb, map);
        renderTopBorder(sb, map);
        renderGrid(sb, map);
        renderBottomBorder(sb, map);
        renderLegend(sb, map);

        return sb.toString();
    }

    private void renderColumnHeader(StringBuilder sb, MapData map) {
        sb.append("     ");
        for (int x = map.minX(); x <= map.maxX(); x++) {
            sb.append(String.format("%2d", x));
        }
        sb.append("\n");
    }

    private void renderTopBorder(StringBuilder sb, MapData map) {
        sb.append("   ").append(MAP_TOP_LEFT);
        sb.append(MAP_HORIZONTAL.repeat(map.maxX() - map.minX() + 1));
        sb.append(MAP_TOP_RIGHT).append("\n");
    }

    private void renderGrid(StringBuilder sb, MapData map) {
        for (int y = map.minY(); y <= map.maxY(); y++) {
            renderRow(sb, map, y);
        }
    }

    private void renderRow(StringBuilder sb, MapData map, int y) {
        sb.append(String.format("%2d %s", y, MAP_VERTICAL));
        for (int x = map.minX(); x <= map.maxX(); x++) {
            Position pos = new Position(x, y);
            renderCell(sb, map, pos);
        }
        sb.append(MAP_VERTICAL).append("\n");
    }

    private void renderCell(StringBuilder sb, MapData map, Position pos) {
        if (pos.equals(map.playerPosition())) {
            sb.append(" ").append(PLAYER_MARKER);
        } else {
            MapData.BiomeData biome = map.getBiomeAt(pos);
            if (biome != null) {
                sb.append(" ").append(getBiomeChar(biome.type()));
            } else {
                sb.append(" .");
            }
        }
    }

    private void renderBottomBorder(StringBuilder sb, MapData map) {
        sb.append("   ").append(MAP_BOTTOM_LEFT);
        sb.append(MAP_HORIZONTAL.repeat(map.maxX() - map.minX() + 1));
        sb.append(MAP_BOTTOM_RIGHT).append("\n");
    }

    private void renderLegend(StringBuilder sb, MapData map) {
        MapData.BiomeData currentBiome = map.getBiomeAt(map.playerPosition());
        if (currentBiome != null) {
            sb.append("\nPosition: ").append(map.playerPosition())
              .append(" - ").append(currentBiome.type())
              .append(" (Level ").append(currentBiome.animalLevel()).append(")\n");
        }
        sb.append("\nLegend: G=Grassland D=Desert T=Tundra J=Jungle F=Forest O=Ocean @=You\n");
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

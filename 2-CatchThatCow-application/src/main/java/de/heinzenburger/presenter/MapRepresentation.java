package de.heinzenburger.presenter;

import de.heinzenburger.Map;
import de.heinzenburger.Position;

import java.util.HashMap;

public class MapRepresentation {
    Map map;

    public MapRepresentation(Map map) {
        this.map = map;
    }

    public String[][] getMapRepresentation() {
        int biomeCellCount = map.getBiomeCellCount();
        String[][] mapRepresentation = new String[biomeCellCount][biomeCellCount];
        for (int y = 0; y < biomeCellCount; y++) {
            for (int x = 0; x < biomeCellCount; x++) {
                mapRepresentation[x][y] = map.getBiomeAt(new Position(x, y)).toShortString();
            }
        }
        return mapRepresentation;
    }

    public HashMap<String, String> getLegend() {
        HashMap<String, String> legend = new HashMap<>();
        for (int x = 0; x < map.getBiomeCellCount(); x++) {
            for (int y = 0; y < map.getBiomeCellCount(); y++) {
                String biomeType = map.getBiomeAt(new Position(x, y)).getType().getName();
                legend.put(biomeType.substring(0, 1), biomeType); // TODO: das sollte die selbe methode sein wie die toShortString() methode der biome, damit es immer übereinstimmt
            }
        }
        return legend;
    }
}

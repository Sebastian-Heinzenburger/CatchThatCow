package de.heinzenburger.presenter;

import de.heinzenburger.Position;

import java.util.HashMap;

public interface MapPresenter {
    void showMap(Position playerPosition, String[][] map);
    void showLegend(HashMap<String, String> legend);
}


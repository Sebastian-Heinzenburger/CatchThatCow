package de.heinzenburger.gameactions;

import de.heinzenburger.Map;
import de.heinzenburger.Player;
import de.heinzenburger.presenter.MapPresenter;
import de.heinzenburger.presenter.MapRepresentation;

public class ViewMap extends GameAction {
    Player player;
    Map map;
    MapPresenter mapPresenter;

    public ViewMap(Player player, Map map, MapPresenter mapPresenter) {
        this.player = player;
        this.map = map;
        this.mapPresenter = mapPresenter;
    }

    @Override
    public void execute() {
        MapRepresentation mapRepresentation = new MapRepresentation(map);
        mapPresenter.showMap(player.getPosition(), mapRepresentation.getMapRepresentation());
        mapPresenter.showLegend(mapRepresentation.getLegend());
    }
}

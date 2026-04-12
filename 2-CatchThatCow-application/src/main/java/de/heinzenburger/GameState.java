package de.heinzenburger;

import de.heinzenburger.gameactions.*;
import de.heinzenburger.presenter.GamePresenter;

import java.util.List;

public class GameState {
    Player player;
    Map map;
    List<GameAction> availableActions;

    GameState(GamePresenter presenter, Random random) {
        int mapSize = 3;
        Position startPosition = new Position(mapSize, mapSize);
        player = new Player(startPosition, new Animal("Kuh"), new Animal("Hase"), new Animal("Eisbär"));
        map = new Map(mapSize, startPosition, random);

        availableActions = List.of( //
                new Explore(), //
                new Move(player, map, presenter), //
                new ViewInventory(player.getInventory(), presenter), //
                new ViewMap(player, map, presenter), //
                new Quit() //
        );
    }

    public List<GameAction> getAvailableActions() {
        return availableActions;
    }
}

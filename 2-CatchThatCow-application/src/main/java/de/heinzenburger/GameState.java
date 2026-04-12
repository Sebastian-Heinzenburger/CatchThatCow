package de.heinzenburger;

import de.heinzenburger.gameactions.*;

import java.util.List;

public class GameState {
    Player player;
    Map map;
    List<GameAction> availableActions;

    GameState(GamePresenter presenter, Random random) {
        player = new Player(new Animal("Kuh"), new Animal("Hase"), new Animal("Eisbär"));
        map = new Map(3, random);

        availableActions = List.of(
                new Explore(),
                new Move(player, map, presenter),
                new ViewInventory(player.getInventory(), presenter),
                new Quit()
        );
    }

    public List<GameAction> getAvailableActions() {
        return availableActions;
    }
}

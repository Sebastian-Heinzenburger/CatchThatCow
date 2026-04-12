package de.heinzenburger;

import de.heinzenburger.gameactions.*;

import java.util.List;

public class GameState {
    List<GameAction> availableActions;

    GameState() {
        availableActions = List.of(
                new Explore(),
                new Move(),
                new ViewInventory(),
                new Quit()
        );
    }

    public List<GameAction> getAvailableActions() {
        return availableActions;
    }
}

package de.heinzenburger.gameactions;

import de.heinzenburger.Direction;
import de.heinzenburger.Map;
import de.heinzenburger.MovementOptions;
import de.heinzenburger.Player;
import de.heinzenburger.presenter.GamePresenter;

public class Move extends GameAction {
    GamePresenter gamePresenter;
    Player player;
    Map map;

    public Move(Player player, Map map, GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
        this.player = player;
        this.map = map;
    }

    @Override
    public void execute() {
        // let player choose direction to move
        MovementOptions options = map.getMovementOptions(player);
        Direction chosenDirection = gamePresenter.chooseDirection(options);
        // move player in chosen direction
        Player.move(chosenDirection);
    }
}

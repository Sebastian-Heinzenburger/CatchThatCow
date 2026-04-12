package de.heinzenburger.presenter;

import de.heinzenburger.Direction;
import de.heinzenburger.MovementOptions;

public interface MoveOptionsPresenter {
    Direction chooseDirection(MovementOptions movementOptions);
}

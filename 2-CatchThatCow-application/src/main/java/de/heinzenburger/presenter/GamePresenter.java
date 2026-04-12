package de.heinzenburger.presenter;

import de.heinzenburger.gameactions.GameAction;

import java.util.List;

public abstract class GamePresenter implements InventoryPresenter, MoveOptionsPresenter {
    public abstract void showWelcomeMessage();

    public abstract GameAction chooseGameAction(List<GameAction> availableActions);
}

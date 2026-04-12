package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;

import java.util.List;

public interface GamePresenter {
        void showWelcomeMessage();
        GameAction chooseGameAction(List<GameAction> availableActions);
}

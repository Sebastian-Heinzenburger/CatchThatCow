package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;
import de.heinzenburger.presenter.GamePresenter;

public class PlayGameUseCase {
    GamePresenter gamePresenter;
    GameState gameState;

    public PlayGameUseCase(GamePresenter gamePresenter, Random random) {
        this.gamePresenter = gamePresenter;
        this.gameState = new GameState(gamePresenter, random);
    }

    public void start() {
        gamePresenter.showWelcomeMessage();
        while (true) {
            GameAction chosenActions = gamePresenter.chooseGameAction(gameState.getAvailableActions());
            chosenActions.execute();
        }
    }
}

package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;

public class PlayGameUseCase {
    GamePresenter gamePresenter;
    GameState gameState;

    public PlayGameUseCase(GamePresenter gamePresenter, Random random) {
        this.gamePresenter = gamePresenter;
        this.gameState = new GameState(gamePresenter, random);
    }

    public void start() {
        gamePresenter.showWelcomeMessage();
        GameAction chosenActions = gamePresenter.chooseGameAction(gameState.getAvailableActions());
        chosenActions.execute();
    }
}

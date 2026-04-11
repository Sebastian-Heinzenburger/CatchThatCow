package de.heinzenburger;

public interface GamePresenter {
    void showWelcome();
    void showMainMenu();
    GameAction getUserAction();
    void showGameOver(String reason);
}

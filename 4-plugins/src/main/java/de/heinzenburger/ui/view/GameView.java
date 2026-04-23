package de.heinzenburger.ui.view;

import de.heinzenburger.session.GameSession;
import de.heinzenburger.ui.command.CommandResult;

/**
 * Interface for game display operations.
 * Abstracts the output mechanism (console, GUI, etc.).
 */
public interface GameView {

    void showWelcome();

    void showMainMenu();

    void showPhasePrompt(GameSession session);

    void showResult(CommandResult result);

    String readInput();

    void showMessage(String message);

    void showError(String message);

    void showPrompt(String prompt);
}

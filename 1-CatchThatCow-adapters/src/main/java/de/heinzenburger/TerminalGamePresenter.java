package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;

import java.util.List;

public class TerminalGamePresenter implements GamePresenter {
    TextPresenter textPresenter;
    TextInput textInput;

    public TerminalGamePresenter(TextPresenter textPresenter, TextInput textInput) {
        this.textPresenter = textPresenter;
        this.textInput = textInput;
    }

    @Override
    public void showWelcomeMessage() {
        textPresenter.print("""
                                                                                                 ▄▄\s
                ▄█████  ▄▄▄ ▄▄▄▄▄▄ ▄▄▄▄ ▄▄ ▄▄   ██████ ▄▄ ▄▄  ▄▄▄ ▄▄▄▄▄▄   ▄█████  ▄▄▄  ▄▄   ▄▄  ██\s
                ██     ██▀██  ██  ██▀▀▀ ██▄██     ██   ██▄██ ██▀██  ██     ██     ██▀██ ██ ▄ ██  ██\s
                ▀█████ ██▀██  ██  ▀████ ██ ██     ██   ██ ██ ██▀██  ██     ▀█████ ▀███▀  ▀█▀█▀   ▄▄\s
                                                                                                   \s""");
    }

    @Override
    public GameAction chooseGameAction(List<GameAction> availableActions) {
        // print all the available game actions
        List<String> actionNames = availableActions.stream().map(GameAction::getName).toList();
        textPresenter.printNumberedList("Available actions:", actionNames);

        // ask the user to choose one of the available game actions
        textPresenter.print("Please choose one of the available actions (1-" + availableActions.size() + "): ");

        int chosenAction = textInput.readInt(1, availableActions.size());
        int chosenActionIndex = chosenAction - 1; // convert to zero-based index
        return availableActions.get(chosenActionIndex);
    }
}

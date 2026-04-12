package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;
import de.heinzenburger.presenter.GamePresenter;

import java.util.HashMap;
import java.util.List;

public class TerminalGamePresenter extends GamePresenter {
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

    @Override
    public void showInventoryItems(List<InventoryItem> animalsInInventory) {
        List<String> itemDescriptions = animalsInInventory.stream().map(animal -> animal.getTitle() + "\n" + animal.getDescription()).toList();
        textPresenter.printNumberedList("Inventar", itemDescriptions);
    }

    public Direction chooseDirection(MovementOptions movementOptions) {
        for (MovementOptions.MovementOption movementOption : movementOptions.getAvailableMovementOptions()) {
            textPresenter.print(movementOption.getDirection().asChar() + ": " + movementOption.getBiom());
        }
        Character[] allowedChars = movementOptions.getAvailableDirections().stream().map(Direction::toChar).toArray(Character[]::new);

        char choice = textInput.readChar(allowedChars);
        return Direction.fromChar(choice);
    }

    @Override
    public void showMap(Position playerPosition, String[][] map) {
        textPresenter.print("Map:");
        // add blue color to the player position
        map[playerPosition.getX()][playerPosition.getY()] = "\u001B[34m" + map[playerPosition.getX()][playerPosition.getY()] + "\u001B[0m";
        for (int y = 0; y < map.length; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < map[y].length; x++) {
                row.append(map[x][y]).append(" ");
            }
            textPresenter.print(row.toString());
        }
    }

    @Override
    public void showLegend(HashMap<String, String> legend) {
        textPresenter.print("Legend:");
        for (String key : legend.keySet()) {
            String value = legend.get(key);
            textPresenter.print(key + ": " + value);
        }
    }

}

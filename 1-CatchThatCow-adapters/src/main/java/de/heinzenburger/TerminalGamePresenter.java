package de.heinzenburger;

import de.heinzenburger.gameactions.GameAction;
import de.heinzenburger.position.MapUnit;
import de.heinzenburger.presenter.GamePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class TerminalGamePresenter extends GamePresenter {
    TextPresenter textPresenter;
    TextInput textInput;

    public TerminalGamePresenter(TextPresenter textPresenter, TextInput textInput) {
        this.textPresenter = textPresenter;
        this.textInput = textInput;
    }

    private static void addBlueToPlayerPosition(Position playerPosition, String[][] map) {
        MapUnit playerX = playerPosition.getX();
        MapUnit playerY = playerPosition.getY();
        map[playerX.toInt()][playerY.toInt()] = "\u001B[34m" + map[playerX.toInt()][playerY.toInt()] + "\u001B[0m";
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
        List<String> actionNames = availableActions.stream().map(GameAction::getName).toList();
        textPresenter.printNumberedList("Available actions:", actionNames);

        textPresenter.print("Please choose one of the available actions (1-" + availableActions.size() + "): ");

        int chosenAction = textInput.readInt(1, availableActions.size());
        int chosenActionIndex = chosenAction - 1; // convert to zero-based index
        return availableActions.get(chosenActionIndex);
    }

    @Override
    public void showInventoryItems(List<InventoryItem> animalsInInventory) {
        Function<InventoryItem, String> describeItem = item -> item.getTitle() + "\n" + item.getDescription();
        List<String> itemDescriptions = animalsInInventory.stream().map(describeItem).toList();
        textPresenter.printNumberedList("Inventar", itemDescriptions);
    }

    public Direction chooseDirection(MovementOptions movementOptions) {
        for (MovementOptions.MovementOption option : movementOptions.getAvailableMovementOptions())
            textPresenter.print(option.getDirection().asChar() + ": " + option.getBiom());

        List<Direction> availableDirections = movementOptions.getAvailableDirections();
        Character[] allowedChars = availableDirections.stream().map(Direction::toChar).toArray(Character[]::new);

        char choice = textInput.readChar(allowedChars);
        return Direction.fromChar(choice);
    }

    @Override
    public void showMap(Position playerPosition, String[][] map) {
        textPresenter.print("Map:");
        addBlueToPlayerPosition(playerPosition, map);
        printMap(map);
    }

    private void printMap(String[][] map) {
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

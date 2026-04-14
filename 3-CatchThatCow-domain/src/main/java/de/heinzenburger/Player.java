package de.heinzenburger;

import java.util.List;

public class Player {
    Inventory inventory;
    Position position;

    public Player(Position startingPosition, InventoryItem... startItems) {
        this.inventory = new Inventory(List.of(startItems));
        this.position = startingPosition;
    }

    public void moveInto(Direction chosenDirection) {
        this.position = this.position.moveInto(chosenDirection);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Position getPosition() {
        return position;
    }
}

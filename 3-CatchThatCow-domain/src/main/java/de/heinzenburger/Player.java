package de.heinzenburger;

import java.util.List;

public class Player {
    Inventory inventory;
    Position position;

    public Player(InventoryItem ...startItems) {
        this.inventory = new Inventory(List.of(startItems));
        this.position = new Position(0, 0);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Position getPosition() {
        return position;
    }
}

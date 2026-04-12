package de.heinzenburger.gameactions;

import de.heinzenburger.GamePresenter;
import de.heinzenburger.Inventory;

public class ViewInventory extends GameAction {
    Inventory inventory;
    GamePresenter gamePresenter;

    public ViewInventory(Inventory inventory, GamePresenter gamePresenter) {
        this.inventory = inventory;
        this.gamePresenter = gamePresenter;
    }

    @Override
    public void execute() {
        gamePresenter.showInventoryItems(inventory.getInventoryItems());
    }
}

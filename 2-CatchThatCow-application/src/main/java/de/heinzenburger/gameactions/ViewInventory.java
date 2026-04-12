package de.heinzenburger.gameactions;

import de.heinzenburger.Inventory;
import de.heinzenburger.presenter.InventoryPresenter;

public class ViewInventory extends GameAction {
    Inventory inventory;
    InventoryPresenter inventoryPresenter;

    public ViewInventory(Inventory inventory, InventoryPresenter inventoryPresenter) {
        this.inventory = inventory;
        this.inventoryPresenter = inventoryPresenter;
    }

    @Override
    public void execute() {
        inventoryPresenter.showInventoryItems(inventory.getInventoryItems());
    }
}

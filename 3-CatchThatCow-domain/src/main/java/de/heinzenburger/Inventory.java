package de.heinzenburger;

import java.util.List;

public class Inventory {
    List<InventoryItem> inventoryItems;

    public Inventory(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }
}

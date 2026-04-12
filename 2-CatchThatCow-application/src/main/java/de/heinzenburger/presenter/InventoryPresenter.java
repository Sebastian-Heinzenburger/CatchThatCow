package de.heinzenburger.presenter;

import de.heinzenburger.InventoryItem;

import java.util.List;

public interface InventoryPresenter {
    void showInventoryItems(List<InventoryItem> animalsInInventory);
}

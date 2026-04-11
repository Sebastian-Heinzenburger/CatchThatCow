package de.heinzenburger;

public class ShowInventoryUseCase {
    private final InventoryPresenter presenter;

    public ShowInventoryUseCase(InventoryPresenter presenter) {
        this.presenter = presenter;
    }

    public void showInventory(Player player) {
        presenter.displayInventory(player.getInventory());
    }
}

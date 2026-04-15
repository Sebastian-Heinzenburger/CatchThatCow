package de.heinzenburger.player.exception;

import de.heinzenburger.animal.AnimalId;

public class AnimalNotFoundException extends InventoryException {

    public AnimalNotFoundException() {
        super("Animal not found in inventory");
    }

    public AnimalNotFoundException(AnimalId id) {
        super("Animal with ID '" + id + "' not found in inventory");
    }
}

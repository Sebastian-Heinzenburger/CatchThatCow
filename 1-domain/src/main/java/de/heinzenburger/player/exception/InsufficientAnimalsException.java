package de.heinzenburger.player.exception;

public class InsufficientAnimalsException extends InventoryException {

    public InsufficientAnimalsException() {
        super("Insufficient animals in inventory");
    }

    public InsufficientAnimalsException(int required, int available) {
        super("Insufficient animals: required " + required + ", but only " + available + " available");
    }
}

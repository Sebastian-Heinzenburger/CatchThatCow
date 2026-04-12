package de.heinzenburger.gameactions;

public class Quit extends GameAction {
    @Override
    public void execute() {
        System.exit(1);
    }
}

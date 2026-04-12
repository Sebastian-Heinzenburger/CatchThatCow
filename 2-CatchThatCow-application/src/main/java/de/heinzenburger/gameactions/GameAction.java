package de.heinzenburger.gameactions;

public abstract class GameAction {
    public abstract void execute();
    public String getName() {
        return this.getClass().getSimpleName();
    }
}

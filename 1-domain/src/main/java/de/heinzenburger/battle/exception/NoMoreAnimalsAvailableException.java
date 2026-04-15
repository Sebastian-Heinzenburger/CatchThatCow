package de.heinzenburger.battle.exception;

public class NoMoreAnimalsAvailableException extends  BattleException {

    public NoMoreAnimalsAvailableException() {
        super("No more animals available for battle");
    }
}

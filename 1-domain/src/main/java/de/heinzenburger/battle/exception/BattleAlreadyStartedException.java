package de.heinzenburger.battle.exception;

public class BattleAlreadyStartedException extends BattleException {

    public BattleAlreadyStartedException() {
        super("Battle has already been started");
    }
}

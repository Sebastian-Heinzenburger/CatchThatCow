package de.heinzenburger.battle.exception;

public class BattleNotInProgressException extends BattleException {

    public BattleNotInProgressException() {
        super("Battle is not in progress");
    }
}

package de.heinzenburger.battle.exception;

public class BattleException extends Exception {

    public BattleException(String message) {
        super(message);
    }

    public BattleException(String message, Throwable cause) {
        super(message, cause);
    }
}

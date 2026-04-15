package de.heinzenburger.battle.exception;

public class NotPlayersTurnException extends BattleException {

    public NotPlayersTurnException() {
        super("It is not the player's turn");
    }

    public NotPlayersTurnException(String message) {
        super(message);
    }
}

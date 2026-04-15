package de.heinzenburger.battle;

/**
 * Encapsulates turn state management in a battle.
 * Tracks whose turn it is and provides methods to switch turns.
 */
public class TurnManager {
    private boolean playerTurn;

    public TurnManager(boolean playerStartsFirst) {
        this.playerTurn = playerStartsFirst;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void switchTurn() {
        this.playerTurn = !this.playerTurn;
    }

}

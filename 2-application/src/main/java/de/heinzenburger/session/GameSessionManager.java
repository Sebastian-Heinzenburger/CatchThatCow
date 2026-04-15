package de.heinzenburger.session;

import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.player.Player;
import de.heinzenburger.world.World;

/**
 * Manages the current game session lifecycle.
 */
public class GameSessionManager {

    private GameSession currentSession;

    public GameSession startSession(Player player, World world) {
        this.currentSession = new GameSession(player, world);
        return this.currentSession;
    }

    public GameSession getCurrentSession() throws GameNotStartedException {
        if (currentSession == null) {
            throw new GameNotStartedException();
        }
        return currentSession;
    }

    public boolean hasActiveSession() {
        return currentSession != null;
    }

    public void endSession() {
        this.currentSession = null;
    }
}

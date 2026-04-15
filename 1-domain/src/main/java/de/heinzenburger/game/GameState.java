package de.heinzenburger.game;

import de.heinzenburger.player.Player;
import de.heinzenburger.world.World;

/**
 * Represents the complete game state that should be persisted.
 */
public record GameState(Player player, World world) {
    public GameState {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (world == null) throw new IllegalArgumentException("World cannot be null");
    }
}


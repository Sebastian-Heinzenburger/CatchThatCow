package de.heinzenburger.game;

import de.heinzenburger.player.Player;
import de.heinzenburger.world.World;

import java.util.Optional;

/**
 * Repository for loading and saving game state atomically.
 * This ensures Player and World are always consistent when loaded/saved together.
 */
public interface GameStateRepository {

    /**
     * Loads the saved game state.
     *
     * @return the game state if present, empty otherwise
     */
    Optional<GameState> load();

    /**
     * Saves the current game state atomically.
     *
     * @param player the player to save
     * @param world the world to save
     */
    void save(Player player, World world);
}

package de.heinzenburger.usecase;

import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.player.PlayerRepository;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.world.WorldRepository;

/**
 * Persists the current game state.
 */
public class SaveGameUseCase {

    private final GameSessionManager sessionManager;
    private final PlayerRepository playerRepository;
    private final WorldRepository worldRepository;

    public SaveGameUseCase(
            GameSessionManager sessionManager,
            PlayerRepository playerRepository,
            WorldRepository worldRepository) {
        this.sessionManager = sessionManager;
        this.playerRepository = playerRepository;
        this.worldRepository = worldRepository;
    }

    /**
     * Saves the current game state.
     *
     * @throws GameNotStartedException if no active game session
     */
    public void execute() throws GameNotStartedException {
        GameSession session = sessionManager.getCurrentSession();

        // Persist player and world
        playerRepository.save(session.getPlayer());
        worldRepository.save(session.getWorld());
    }
}

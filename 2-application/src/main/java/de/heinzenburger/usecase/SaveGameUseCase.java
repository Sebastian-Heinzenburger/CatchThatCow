package de.heinzenburger.usecase;

import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.game.GameStateRepository;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;

/**
 * Persists the current game state.
 */
public class SaveGameUseCase {

    private final GameSessionManager sessionManager;
    private final GameStateRepository gameStateRepository;

    public SaveGameUseCase(
            GameSessionManager sessionManager,
            GameStateRepository gameStateRepository) {
        this.sessionManager = sessionManager;
        this.gameStateRepository = gameStateRepository;
    }

    /**
     * Saves the current game state.
     *
     * @throws GameNotStartedException if no active game session
     */
    public void execute() throws GameNotStartedException {
        GameSession session = sessionManager.getCurrentSession();

        // Persist player and world atomically
        gameStateRepository.save(session.getPlayer(), session.getWorld());
    }
}

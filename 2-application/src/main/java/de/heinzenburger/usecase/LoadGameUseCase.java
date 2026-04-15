package de.heinzenburger.usecase;

import de.heinzenburger.exception.ApplicationException;
import de.heinzenburger.game.GameState;
import de.heinzenburger.game.GameStateRepository;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;

/**
 * Loads a previously saved game state.
 */
public class LoadGameUseCase {

    private final GameSessionManager sessionManager;
    private final GameStateRepository gameStateRepository;

    public LoadGameUseCase(GameSessionManager sessionManager, GameStateRepository gameStateRepository) {
        this.sessionManager = sessionManager;
        this.gameStateRepository = gameStateRepository;
    }

    /**
     * Loads a saved game and starts a new session with the loaded data.
     *
     * @return the loaded game session
     * @throws NoSavedGameException if no saved game exists
     */
    public GameSession execute() throws NoSavedGameException {
        // Load player and world atomically
        GameState gameState = gameStateRepository.load().orElseThrow(NoSavedGameException::new);

        // End any existing session and start with loaded data
        sessionManager.endSession();
        return sessionManager.startSession(gameState.player(), gameState.world());
    }

    /**
     * Exception thrown when no saved game exists to load.
     */
    public static class NoSavedGameException extends ApplicationException {
        public NoSavedGameException() {
            super("No saved game found");
        }
    }
}

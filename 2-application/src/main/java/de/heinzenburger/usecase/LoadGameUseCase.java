package de.heinzenburger.usecase;

import de.heinzenburger.exception.ApplicationException;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.PlayerRepository;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.world.World;
import de.heinzenburger.world.WorldRepository;

import java.util.Optional;

/**
 * Loads a previously saved game state.
 */
public class LoadGameUseCase {

    private final GameSessionManager sessionManager;
    private final PlayerRepository playerRepository;
    private final WorldRepository worldRepository;

    public LoadGameUseCase(GameSessionManager sessionManager, PlayerRepository playerRepository, WorldRepository worldRepository) {
        this.sessionManager = sessionManager;
        this.playerRepository = playerRepository;
        this.worldRepository = worldRepository;
    }

    /**
     * Loads a saved game and starts a new session with the loaded data.
     *
     * @return the loaded game session
     * @throws NoSavedGameException if no saved game exists
     */
    public GameSession execute() throws NoSavedGameException {
        // Load player and world
        // TODO: Player and World should be loaded together in a single transaction to ensure consistency
        Optional<Player> playerOpt = playerRepository.load();
        Optional<World> worldOpt = worldRepository.load();

        if (playerOpt.isEmpty() || worldOpt.isEmpty()) throw new NoSavedGameException();

        // End any existing session and start with loaded data
        sessionManager.endSession();
        return sessionManager.startSession(playerOpt.get(), worldOpt.get());
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

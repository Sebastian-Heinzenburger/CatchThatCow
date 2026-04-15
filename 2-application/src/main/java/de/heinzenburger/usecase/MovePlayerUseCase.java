package de.heinzenburger.usecase;

import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.exception.InvalidMoveException;
import de.heinzenburger.player.Player;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Direction;
import de.heinzenburger.shared.Position;
import de.heinzenburger.shared.ValidPosition;
import de.heinzenburger.world.World;

/**
 * Handles player navigation in the world.
 */
public class MovePlayerUseCase {

    private final GameSessionManager sessionManager;

    public MovePlayerUseCase(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Moves the player in the given direction.
     *
     * @param direction the direction to move
     * @return the player's new position TODO: do we need to return the new position?
     * @throws GameNotStartedException   if no active game session
     * @throws InvalidGamePhaseException if not in EXPLORING phase
     * @throws InvalidMoveException      if target position is invalid
     */
    public Position execute(Direction direction) throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        GameSession session = sessionManager.getCurrentSession();

        // Validate phase
        if (session.getPhase() != GamePhase.EXPLORING)
            throw new InvalidGamePhaseException(session.getPhase(), GamePhase.EXPLORING);

        Player player = session.getPlayer();
        World world = session.getWorld();
        Position currentPosition = player.getCurrentPosition();

        // Calculate new position based on direction
        Position newPosition = currentPosition.neighbour(direction);

        // Validate the new position exists in the world using type-safe validation
        ValidPosition validPosition;
        try {
            validPosition = ValidPosition.of(newPosition, world);
        } catch (de.heinzenburger.shared.exception.InvalidPositionException e) {
            throw new InvalidMoveException(currentPosition, direction);
        }

        // Move player
        player.moveTo(validPosition);

        return newPosition;
    }

}
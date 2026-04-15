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
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovePlayerUseCaseTest {

    private GameSessionManager sessionManager;
    private MovePlayerUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        useCase = new MovePlayerUseCase(sessionManager);
    }

    @Test
    void shouldMovePlayerNorth() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Position newPosition = useCase.execute(Direction.NORTH);

        assertEquals(new Position(0, -1), newPosition);
        assertEquals(new Position(0, -1), player.getCurrentPosition());
    }

    @Test
    void shouldMovePlayerSouth() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Position newPosition = useCase.execute(Direction.SOUTH);

        assertEquals(new Position(0, 1), newPosition);
        assertEquals(new Position(0, 1), player.getCurrentPosition());
    }

    @Test
    void shouldMovePlayerEast() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Position newPosition = useCase.execute(Direction.EAST);

        assertEquals(new Position(1, 0), newPosition);
        assertEquals(new Position(1, 0), player.getCurrentPosition());
    }

    @Test
    void shouldMovePlayerWest() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Position newPosition = useCase.execute(Direction.WEST);

        assertEquals(new Position(-1, 0), newPosition);
        assertEquals(new Position(-1, 0), player.getCurrentPosition());
    }

    @Test
    void shouldReturnNewPosition() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Position returnedPosition = useCase.execute(Direction.NORTH);

        assertNotNull(returnedPosition);
        assertEquals(player.getCurrentPosition(), returnedPosition);
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        assertThrows(GameNotStartedException.class, () -> useCase.execute(Direction.NORTH));
    }

    @Test
    void shouldThrowExceptionWhenNotInExploringPhase() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        // Simulate being in encounter phase
        session.setEncounter(TestDataFactory.createPrey("TestPrey", 1));

        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute(Direction.NORTH));
    }

    @Test
    void shouldThrowExceptionWhenMoveOutOfBounds() {
        World world = TestDataFactory.createTestWorld(1); // Small world: -1 to 1
        Player player = TestDataFactory.createPlayerAtPosition(new Position(1, 0), 3);
        sessionManager.startSession(player, world);

        // Try to move east beyond boundary
        assertThrows(InvalidMoveException.class, () -> useCase.execute(Direction.EAST));
    }

    @Test
    void shouldStayInExploringPhaseAfterMove() throws GameNotStartedException, InvalidGamePhaseException, InvalidMoveException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        useCase.execute(Direction.NORTH);

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }
}

package de.heinzenburger.usecase;

import de.heinzenburger.player.Player;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.usecase.LoadGameUseCase.NoSavedGameException;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoadGameUseCaseTest {

    private GameSessionManager sessionManager;
    private RepositoryStubs.InMemoryGameStateRepository gameStateRepository;
    private LoadGameUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        gameStateRepository = new RepositoryStubs.InMemoryGameStateRepository();
        useCase = new LoadGameUseCase(sessionManager, gameStateRepository);
    }

    @Test
    void shouldLoadPlayerFromRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        gameStateRepository.save(player, world);

        GameSession session = useCase.execute();

        assertEquals(player, session.getPlayer());
    }

    @Test
    void shouldLoadWorldFromRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        gameStateRepository.save(player, world);

        GameSession session = useCase.execute();

        assertEquals(world, session.getWorld());
    }

    @Test
    void shouldCreateNewSessionWithLoadedData() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        gameStateRepository.save(player, world);

        GameSession session = useCase.execute();

        assertNotNull(session);
        assertTrue(sessionManager.hasActiveSession());
        assertEquals(session, sessionManager.getCurrentSession());
    }

    @Test
    void shouldEndExistingSessionBeforeLoading() throws Exception {
        // Create an existing session
        World existingWorld = TestDataFactory.createTestWorld(1);
        Player existingPlayer = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 2);
        sessionManager.startSession(existingPlayer, existingWorld);
        assertTrue(sessionManager.hasActiveSession());

        // Save different game state
        World savedWorld = TestDataFactory.createTestWorld(3);
        Player savedPlayer = TestDataFactory.createPlayerAtPosition(new Position(1, 1), 5);
        gameStateRepository.save(savedPlayer, savedWorld);

        // Load should end existing session and create new one
        GameSession newSession = useCase.execute();

        assertEquals(savedPlayer, newSession.getPlayer());
        assertEquals(savedWorld, newSession.getWorld());
    }

    @Test
    void shouldTransitionToExploringPhase() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        gameStateRepository.save(player, world);

        GameSession session = useCase.execute();

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }

    @Test
    void shouldThrowExceptionWhenNoSavedGame() {
        // Nothing saved
        assertThrows(NoSavedGameException.class, () -> useCase.execute());
    }
}

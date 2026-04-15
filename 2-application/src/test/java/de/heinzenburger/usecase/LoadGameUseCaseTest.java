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
    private RepositoryStubs.InMemoryPlayerRepository playerRepository;
    private RepositoryStubs.InMemoryWorldRepository worldRepository;
    private LoadGameUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        playerRepository = new RepositoryStubs.InMemoryPlayerRepository();
        worldRepository = new RepositoryStubs.InMemoryWorldRepository();
        useCase = new LoadGameUseCase(sessionManager, playerRepository, worldRepository);
    }

    @Test
    void shouldLoadPlayerFromRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        playerRepository.save(player);
        worldRepository.save(world);

        GameSession session = useCase.execute();

        assertEquals(player, session.getPlayer());
    }

    @Test
    void shouldLoadWorldFromRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        playerRepository.save(player);
        worldRepository.save(world);

        GameSession session = useCase.execute();

        assertEquals(world, session.getWorld());
    }

    @Test
    void shouldCreateNewSessionWithLoadedData() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        playerRepository.save(player);
        worldRepository.save(world);

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
        playerRepository.save(savedPlayer);
        worldRepository.save(savedWorld);

        // Load should end existing session and create new one
        GameSession newSession = useCase.execute();

        assertEquals(savedPlayer, newSession.getPlayer());
        assertEquals(savedWorld, newSession.getWorld());
    }

    @Test
    void shouldTransitionToExploringPhase() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);

        playerRepository.save(player);
        worldRepository.save(world);

        GameSession session = useCase.execute();

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }

    @Test
    void shouldThrowExceptionWhenNoSavedPlayer() {
        World world = TestDataFactory.createTestWorld(2);
        worldRepository.save(world);

        // No player saved
        assertThrows(NoSavedGameException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenNoSavedWorld() {
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        playerRepository.save(player);

        // No world saved
        assertThrows(NoSavedGameException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenNeitherExists() {
        // Nothing saved
        assertThrows(NoSavedGameException.class, () -> useCase.execute());
    }
}

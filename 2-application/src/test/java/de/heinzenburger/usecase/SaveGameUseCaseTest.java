package de.heinzenburger.usecase;

import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.player.Player;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaveGameUseCaseTest {

    private GameSessionManager sessionManager;
    private RepositoryStubs.InMemoryPlayerRepository playerRepository;
    private RepositoryStubs.InMemoryWorldRepository worldRepository;
    private SaveGameUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        playerRepository = new RepositoryStubs.InMemoryPlayerRepository();
        worldRepository = new RepositoryStubs.InMemoryWorldRepository();
        useCase = new SaveGameUseCase(sessionManager, playerRepository, worldRepository);
    }

    @Test
    void shouldSavePlayerToRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        useCase.execute();

        assertTrue(playerRepository.load().isPresent());
        assertEquals(player, playerRepository.load().get());
    }

    @Test
    void shouldSaveWorldToRepository() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        useCase.execute();

        assertTrue(worldRepository.load().isPresent());
        assertEquals(world, worldRepository.load().get());
    }

    @Test
    void shouldSaveInExploringPhase() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        assertEquals(GamePhase.EXPLORING, session.getPhase());

        useCase.execute();

        assertTrue(playerRepository.load().isPresent());
        assertTrue(worldRepository.load().isPresent());
    }

    @Test
    void shouldSaveInEncounterPendingPhase() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        // Simulate encounter
        session.setEncounter(TestDataFactory.createPrey("Rabbit", 1));

        useCase.execute();

        assertTrue(playerRepository.load().isPresent());
        assertTrue(worldRepository.load().isPresent());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        assertThrows(GameNotStartedException.class, () -> useCase.execute());
    }
}

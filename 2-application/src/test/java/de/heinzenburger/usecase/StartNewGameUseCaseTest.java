package de.heinzenburger.usecase;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.services.AnimalEncounterService;
import de.heinzenburger.services.WorldGenerator;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.RandomNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartNewGameUseCaseTest {

    private GameSessionManager sessionManager;
    private StartNewGameUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        WorldGenerator worldGenerator = new WorldGenerator(TestDataFactory.getTestRandom());
        RepositoryStubs.InMemoryAnimalSpeciesRepository speciesRepository = new RepositoryStubs.InMemoryAnimalSpeciesRepository();

        // Add test species - create enough for all biome types (GRASSLAND, FOREST, DESERT, TUNDRA, JUNGLE)
        // Each biome type needs at least one species
        List<AnimalSpecies> species = TestDataFactory.createAnimalSpeciesList(15, 1);
        species.forEach(speciesRepository::add);

        RandomNumberGenerator random = TestDataFactory.getTestRandom();
        AnimalEncounterService encounterService = new AnimalEncounterService(random);

        useCase = new StartNewGameUseCase(worldGenerator, speciesRepository, encounterService, sessionManager, random);
    }

    @Test
    void shouldCreateNewGameWithGeneratedWorld() {
        int worldSize = 2;

        GameSession session = useCase.execute(worldSize);

        assertNotNull(session);
        assertNotNull(session.getWorld());
        assertNotNull(session.getWorld().getStartPosition());
    }

    @Test
    void shouldCreatePlayerAtWorldStartPosition() {
        int worldSize = 2;

        GameSession session = useCase.execute(worldSize);

        assertEquals(session.getWorld().getStartPosition(), session.getPlayer().getCurrentPosition());
    }

    @Test
    void shouldGivePlayerThreeStarterAnimals() {
        int worldSize = 2;

        GameSession session = useCase.execute(worldSize);

        assertEquals(3, session.getPlayer().getInventory().size());
    }

    @Test
    void shouldStarterAnimalsMatchStartBiome() {
        int worldSize = 2;

        GameSession session = useCase.execute(worldSize);

        // Verify animals are level 1 (matching start biome)
        session.getPlayer().getInventory().getAnimals().forEach(animal -> assertEquals(1, animal.getSpecies().getLevel()));
    }

    @Test
    void shouldEndExistingSessionBeforeStartingNew() throws GameNotStartedException {
        // Start first session
        useCase.execute(2);
        assertTrue(sessionManager.hasActiveSession());

        // Start second session
        GameSession newSession = useCase.execute(3);

        // Should have new session
        assertNotNull(newSession);
        assertEquals(newSession, sessionManager.getCurrentSession());
    }

    @Test
    void shouldReturnGameSessionInExploringPhase() {
        GameSession session = useCase.execute(2);

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }
}

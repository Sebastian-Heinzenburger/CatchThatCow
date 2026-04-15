package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.services.AnimalEncounterService;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriggerEncounterUseCaseTest {

    private GameSessionManager sessionManager;
    private AnimalEncounterService encounterService;
    private RepositoryStubs.InMemoryAnimalSpeciesRepository speciesRepository;
    private TriggerEncounterUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        encounterService = new AnimalEncounterService(TestDataFactory.getTestRandom());
        speciesRepository = new RepositoryStubs.InMemoryAnimalSpeciesRepository();

        // Add test species
        List<AnimalSpecies> species = TestDataFactory.createAnimalSpeciesList(5, 1);
        species.forEach(speciesRepository::add);

        useCase = new TriggerEncounterUseCase(sessionManager, encounterService, speciesRepository);
    }

    @Test
    void shouldGenerateRandomAnimalEncounter() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Animal encounteredAnimal = useCase.execute();

        assertNotNull(encounteredAnimal);
    }

    @Test
    void shouldEncounterAnimalFromCurrentBiome() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        Animal encounteredAnimal = useCase.execute();

        // Animal should be level 1 (matching the biome level)
        assertEquals(1, encounteredAnimal.getSpecies().getLevel());
    }

    @Test
    void shouldTransitionToEncounterPendingPhase() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        useCase.execute();

        assertEquals(GamePhase.ENCOUNTER_PENDING, session.getPhase());
    }

    @Test
    void shouldStoreEncounteredAnimalInSession() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        Animal encounteredAnimal = useCase.execute();

        assertEquals(encounteredAnimal, session.getEncounteredAnimal());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        assertThrows(GameNotStartedException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenNotInExploringPhase() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        // Trigger first encounter
        useCase.execute();

        // Try to trigger another encounter while in ENCOUNTER_PENDING phase
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute());
    }
}

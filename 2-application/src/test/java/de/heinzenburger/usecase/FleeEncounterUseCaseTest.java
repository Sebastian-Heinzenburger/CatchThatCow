package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.usecase.FleeEncounterUseCase.CannotFleeFromPredatorException;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FleeEncounterUseCaseTest {

    private GameSessionManager sessionManager;
    private FleeEncounterUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        useCase = new FleeEncounterUseCase(sessionManager);
    }

    @Test
    void shouldFleeFromPreyAnimal() throws GameNotStartedException, CannotFleeFromPredatorException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        // Set up encounter with prey
        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        useCase.execute();

        // Should succeed without exception
        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }

    @Test
    void shouldTransitionBackToExploringPhase() throws GameNotStartedException, CannotFleeFromPredatorException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        useCase.execute();

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }

    @Test
    void shouldClearEncounteredAnimal() throws GameNotStartedException, CannotFleeFromPredatorException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        useCase.execute();

        assertNull(session.getEncounteredAnimal());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        assertThrows(GameNotStartedException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenNotInEncounterPendingPhase() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        sessionManager.startSession(player, world);

        // Phase is EXPLORING, not ENCOUNTER_PENDING
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenFleeingFromPredator() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        // Set up encounter with predator
        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        assertThrows(CannotFleeFromPredatorException.class, () -> useCase.execute());
    }

    @Test
    void shouldIncludePredatorInException() throws GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 3);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        try {
            useCase.execute();
            fail("Should have thrown CannotFleeFromPredatorException");
        } catch (CannotFleeFromPredatorException e) {
            assertEquals(predator, e.getPredator());
        }
    }
}

package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.BattleState;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.services.BattleFactory;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StartBattleUseCaseTest {

    private GameSessionManager sessionManager;
    private BattleFactory battleFactory;
    private StartBattleUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        battleFactory = new BattleFactory(TestDataFactory.getTestRandom());
        useCase = new StartBattleUseCase(sessionManager, battleFactory);
    }

    @Test
    void shouldCreateAndStartBattle() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        Battle battle = useCase.execute();

        assertNotNull(battle);
        assertInstanceOf(BattleState.InProgress.class, battle.getState());
    }

    @Test
    void shouldSelectThreeRandomAnimalsForBattle() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        Battle battle = useCase.execute();

        // Battle inventory should have 3 animals
        assertEquals(3, battle.getAllBattleAnimals().size());
    }

    @Test
    void shouldTransitionToInBattlePhase() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        useCase.execute();

        assertEquals(GamePhase.IN_BATTLE, session.getPhase());
    }

    @Test
    void shouldSetBattleInSession() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        Battle battle = useCase.execute();

        assertEquals(battle, session.getCurrentBattle());
    }

    @Test
    void shouldClearEncounteredAnimalFromSession() throws Exception {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
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
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        sessionManager.startSession(player, world);

        // Phase is EXPLORING, not ENCOUNTER_PENDING
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenPlayerHasInsufficientAnimals() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 0); // No animals
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        assertThrows(InsufficientAnimalsException.class, () -> useCase.execute());
    }
}

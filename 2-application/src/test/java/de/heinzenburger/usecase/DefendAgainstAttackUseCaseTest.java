package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.RoundResult;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.BattleAlreadyStartedException;
import de.heinzenburger.battle.exception.NoMoreAnimalsAvailableException;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.services.BattleFactory;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.Position;
import de.heinzenburger.shared.StatCategory;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefendAgainstAttackUseCaseTest {

    private GameSessionManager sessionManager;
    private BattleFactory battleFactory;
    private DefendAgainstAttackUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        battleFactory = new BattleFactory(TestDataFactory.getTestRandom());
        useCase = new DefendAgainstAttackUseCase(sessionManager);
    }

    @Test
    void shouldGetOpponentSelectedCategory() throws InsufficientAnimalsException, BattleAlreadyStartedException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        // Use predator so opponent goes first
        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        StatCategory category = useCase.getOpponentSelectedCategory();

        assertNotNull(category);
    }

    @Test
    void shouldExecuteDefenseWithSelectedAnimal() throws InsufficientAnimalsException, BattleAlreadyStartedException, GameNotStartedException, NoMoreAnimalsAvailableException, AnimalNotAvailableException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        RoundResult result = useCase.execute(selectedAnimal);

        assertNotNull(result);
    }

    @Test
    void shouldReturnRoundResult() throws InsufficientAnimalsException, BattleAlreadyStartedException, GameNotStartedException, NoMoreAnimalsAvailableException, AnimalNotAvailableException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        RoundResult result = useCase.execute(selectedAnimal);

        assertNotNull(result.getWinner());
        assertNotNull(result.getPlayerAnimal());
        assertNotNull(result.getOpponentAnimal());
    }

    @Test
    void shouldSwitchToPlayerTurnAfterDefense() throws InsufficientAnimalsException, BattleAlreadyStartedException, GameNotStartedException, NoMoreAnimalsAvailableException, AnimalNotAvailableException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        useCase.execute(selectedAnimal);

        assertTrue(battle.isPlayerTurn());
    }

    @Test
    void shouldRemainInBattlePhase() throws InsufficientAnimalsException, BattleAlreadyStartedException, GameNotStartedException, NoMoreAnimalsAvailableException, AnimalNotAvailableException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        useCase.execute(selectedAnimal);

        assertEquals(GamePhase.IN_BATTLE, session.getPhase());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        Animal animal = TestDataFactory.createAnimal("Test", 1, de.heinzenburger.shared.AnimalType.PREY);
        assertThrows(GameNotStartedException.class, () -> useCase.execute(animal));
    }

    @Test
    void shouldThrowExceptionWhenNotInBattlePhase() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        sessionManager.startSession(player, world);

        Animal animal = TestDataFactory.createAnimal("Test", 1, de.heinzenburger.shared.AnimalType.PREY);
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute(animal));
    }

    @Test
    void shouldThrowExceptionWhenPlayerTurn() throws InsufficientAnimalsException, BattleAlreadyStartedException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        // Use prey so player goes first
        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        Battle battle = battleFactory.createBattle(player, prey);
        battle.startBattle();
        session.startBattle(battle);

        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute(selectedAnimal));
    }

    @Test
    void shouldThrowExceptionWhenAnimalNotAvailable() throws InsufficientAnimalsException, BattleAlreadyStartedException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal predator = TestDataFactory.createPredator("Wolf", 1);
        session.setEncounter(predator);

        Battle battle = battleFactory.createBattle(player, predator);
        battle.startBattle();
        session.startBattle(battle);

        // Use an animal not in battle inventory
        Animal invalidAnimal = TestDataFactory.createAnimal("NotInBattle", 1, de.heinzenburger.shared.AnimalType.PREY);
        assertThrows(AnimalNotAvailableException.class, () -> useCase.execute(invalidAnimal));
    }
}

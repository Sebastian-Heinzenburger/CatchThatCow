package de.heinzenburger.usecase;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.exception.*;
import de.heinzenburger.exception.GameNotStartedException;
import de.heinzenburger.exception.InvalidGamePhaseException;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.services.BattleFactory;
import de.heinzenburger.session.GamePhase;
import de.heinzenburger.session.GameSession;
import de.heinzenburger.session.GameSessionManager;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.Position;
import de.heinzenburger.shared.StatCategory;
import de.heinzenburger.usecase.ResolveBattleUseCase.BattleOutcome;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolveBattleUseCaseTest {

    private GameSessionManager sessionManager;
    private BattleFactory battleFactory;
    private ResolveBattleUseCase useCase;

    @BeforeEach
    void setUp() {
        sessionManager = new GameSessionManager();
        battleFactory = new BattleFactory(TestDataFactory.getTestRandom());
        useCase = new ResolveBattleUseCase(sessionManager);
    }

    @Test
    void shouldAwardOpponentAnimalWhenPlayerWins() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        // Create weak opponent so player wins
        Animal weakPrey = TestDataFactory.createAnimalWithStats("WeakRabbit", 1, AnimalType.PREY, 10, 10, 10, 10, 10);
        session.setEncounter(weakPrey);

        Battle battle = battleFactory.createBattle(player, weakPrey);
        battle.startBattle();
        session.startBattle(battle);

        int initialInventorySize = player.getInventory().size();

        // Play rounds until player wins (3 rounds)
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        BattleOutcome outcome = useCase.execute();

        // Player should have gained the opponent animal
        assertEquals(initialInventorySize + 1, player.getInventory().size());
        assertEquals(weakPrey, outcome.caughtAnimal());
        assertNull(outcome.lostAnimal());
    }

    @Test
    void shouldReturnBattleOutcomeWithCaughtAnimal() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal weakPrey = TestDataFactory.createAnimalWithStats("WeakRabbit", 1, AnimalType.PREY, 10, 10, 10, 10, 10);
        session.setEncounter(weakPrey);

        Battle battle = battleFactory.createBattle(player, weakPrey);
        battle.startBattle();
        session.startBattle(battle);

        // Play rounds until player wins
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        BattleOutcome outcome = useCase.execute();

        assertTrue(outcome.playerWon());
        assertNotNull(outcome.caughtAnimal());
        assertNull(outcome.lostAnimal());
    }

    @Test
    void shouldRemoveRandomAnimalWhenPlayerLoses() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        // Create player with weak animals
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 0);
        for (int i = 0; i < 5; i++) {
            player.addAnimal(TestDataFactory.createAnimalWithStats("WeakAnimal" + i, 1, AnimalType.PREY, 10, 10, 10, 10, 10));
        }
        GameSession session = sessionManager.startSession(player, world);

        // Create strong predator
        Animal strongPredator = TestDataFactory.createAnimalWithStats("StrongWolf", 2, AnimalType.PREDATOR, 100, 100, 100, 100, 100);
        session.setEncounter(strongPredator);

        Battle battle = battleFactory.createBattle(player, strongPredator);
        battle.startBattle();
        session.startBattle(battle);

        int initialInventorySize = player.getInventory().size();

        // Play rounds until opponent wins
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        BattleOutcome outcome = useCase.execute();

        // Player should have lost one animal
        assertEquals(initialInventorySize - 1, player.getInventory().size());
        assertNull(outcome.caughtAnimal());
        assertNotNull(outcome.lostAnimal());
    }

    @Test
    void shouldReturnBattleOutcomeWithLostAnimal() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 0);
        for (int i = 0; i < 5; i++) {
            player.addAnimal(TestDataFactory.createAnimalWithStats("WeakAnimal" + i, 1, AnimalType.PREY, 10, 10, 10, 10, 10));
        }
        GameSession session = sessionManager.startSession(player, world);

        Animal strongPredator = TestDataFactory.createAnimalWithStats("StrongWolf", 2, AnimalType.PREDATOR, 100, 100, 100, 100, 100);
        session.setEncounter(strongPredator);

        Battle battle = battleFactory.createBattle(player, strongPredator);
        battle.startBattle();
        session.startBattle(battle);

        // Play rounds until opponent wins
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        BattleOutcome outcome = useCase.execute();

        assertFalse(outcome.playerWon());
        assertNull(outcome.caughtAnimal());
        assertNotNull(outcome.lostAnimal());
    }

    @Test
    void shouldTransitionToExploringPhase() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal weakPrey = TestDataFactory.createAnimalWithStats("WeakRabbit", 1, AnimalType.PREY, 10, 10, 10, 10, 10);
        session.setEncounter(weakPrey);

        Battle battle = battleFactory.createBattle(player, weakPrey);
        battle.startBattle();
        session.startBattle(battle);

        // Play rounds until finished
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        useCase.execute();

        assertEquals(GamePhase.EXPLORING, session.getPhase());
    }

    @Test
    void shouldClearBattleFromSession() throws InsufficientAnimalsException, BattleAlreadyStartedException, BattleNotInProgressException, NotPlayersTurnException, AnimalNotAvailableException, NoMoreAnimalsAvailableException, GameNotStartedException, InvalidGamePhaseException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal weakPrey = TestDataFactory.createAnimalWithStats("WeakRabbit", 1, AnimalType.PREY, 10, 10, 10, 10, 10);
        session.setEncounter(weakPrey);

        Battle battle = battleFactory.createBattle(player, weakPrey);
        battle.startBattle();
        session.startBattle(battle);

        // Play rounds until finished
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        useCase.execute();

        assertNull(session.getCurrentBattle());
    }

    @Test
    void shouldThrowExceptionWhenNoActiveSession() {
        assertThrows(GameNotStartedException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenNotInBattlePhase() {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        sessionManager.startSession(player, world);

        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute());
    }

    @Test
    void shouldThrowExceptionWhenBattleNotFinished() throws BattleAlreadyStartedException, InsufficientAnimalsException {
        World world = TestDataFactory.createTestWorld(2);
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 5);
        GameSession session = sessionManager.startSession(player, world);

        Animal prey = TestDataFactory.createPrey("Rabbit", 1);
        session.setEncounter(prey);

        Battle battle = battleFactory.createBattle(player, prey);
        battle.startBattle();
        session.startBattle(battle);

        // Battle is not finished yet
        assertThrows(InvalidGamePhaseException.class, () -> useCase.execute());
    }

    @Test
    void shouldHandlePlayerWithNoAnimalsToLose() throws GameNotStartedException, InvalidGamePhaseException, BattleNotInProgressException, NotPlayersTurnException, NoMoreAnimalsAvailableException, AnimalNotAvailableException, BattleAlreadyStartedException, InsufficientAnimalsException {
        World world = TestDataFactory.createTestWorld(2);
        // Create player with exactly 3 animals for battle
        Player player = TestDataFactory.createPlayerAtPosition(new Position(0, 0), 0);
        for (int i = 0; i < 3; i++) {
            player.addAnimal(TestDataFactory.createAnimalWithStats("WeakAnimal" + i, 1, AnimalType.PREY, 10, 10, 10, 10, 10));
        }
        GameSession session = sessionManager.startSession(player, world);

        Animal strongPredator = TestDataFactory.createAnimalWithStats("StrongWolf", 2, AnimalType.PREDATOR, 100, 100, 100, 100, 100);
        session.setEncounter(strongPredator);

        Battle battle = battleFactory.createBattle(player, strongPredator);
        battle.startBattle();
        session.startBattle(battle);

        // BattleFactory selects 3 animals for battle, but they remain in player's inventory
        // So inventory still has 3 animals
        int inventorySizeBeforeBattle = player.getInventory().size();
        assertEquals(3, inventorySizeBeforeBattle);

        // Play rounds until opponent wins
        while (!battle.isFinished()) {
            if (battle.isPlayerTurn()) {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.playerAttack(animal, StatCategory.SPEED);
            } else {
                Animal animal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(animal);
            }
        }

        // Should handle gracefully - remove one random animal
        BattleOutcome outcome = useCase.execute();

        assertNotNull(outcome);
        assertFalse(outcome.playerWon());
        assertEquals(inventorySizeBeforeBattle - 1, player.getInventory().size());
        assertNotNull(outcome.lostAnimal()); // Should have lost an animal
    }
}

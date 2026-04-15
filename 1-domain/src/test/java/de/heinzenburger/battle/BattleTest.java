package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.battle.exception.AnimalNotAvailableException;
import de.heinzenburger.battle.exception.BattleAlreadyStartedException;
import de.heinzenburger.battle.exception.BattleNotInProgressException;
import de.heinzenburger.battle.exception.NotPlayersTurnException;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    @Test
    void shouldCreateBattleWithPredator() {
        Animal predator = createPredator();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(predator, battleInventory, new RandomTestAdapter());

        assertEquals(BattleState.NOT_STARTED, battle.getState());
        assertEquals(predator, battle.getOpponentAnimal());
        assertEquals(0, battle.getPlayerScore());
        assertEquals(0, battle.getOpponentScore());
        assertFalse(battle.isPlayerTurn()); // Predator attacks first
    }

    @Test
    void shouldCreateBattleWithPrey() {
        Animal prey = createPrey();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(prey, battleInventory, new RandomTestAdapter());

        assertTrue(battle.isPlayerTurn()); // Player can attack first with prey
    }

    @Test
    void shouldStartBattle() throws BattleAlreadyStartedException {
        Battle battle = createTestBattle();
        battle.startBattle();

        assertEquals(BattleState.IN_PROGRESS, battle.getState());
    }

    @Test
    void shouldNotStartBattleTwice() throws BattleAlreadyStartedException {
        Battle battle = createTestBattle();
        battle.startBattle();

        assertThrows(BattleAlreadyStartedException.class, battle::startBattle);
    }

    @Test
    void shouldExecutePlayerAttackAndIncreasePlayerScore() throws Exception {
        Animal opponent = createPreyWithStats(50, 50, 50, 50, 50);
        List<Animal> playerAnimals = List.of(createAnimalWithStats(100, 100, 100, 100, 100));
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        RoundResult result = battle.playerAttack(playerAnimals.get(0), StatCategory.SPEED);

        assertEquals(RoundWinner.PLAYER, result.getWinner());
        assertEquals(1, battle.getPlayerScore());
        assertEquals(0, battle.getOpponentScore());
        assertFalse(battle.isPlayerTurn()); // Turn switches to opponent
    }

    @Test
    void shouldExecutePlayerAttackAndIncreaseOpponentScore() throws Exception {
        Animal opponent = createPreyWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = List.of(createAnimalWithStats(50, 50, 50, 50, 50));
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        RoundResult result = battle.playerAttack(playerAnimals.get(0), StatCategory.SPEED);

        assertEquals(RoundWinner.OPPONENT, result.getWinner());
        assertEquals(0, battle.getPlayerScore());
        assertEquals(1, battle.getOpponentScore());
    }

    @Test
    void shouldNotAllowPlayerAttackWhenNotPlayerTurn() throws BattleAlreadyStartedException {
        Battle battle = createTestBattle();
        battle.startBattle();

        // Simulate opponent's turn
        Battle opponentTurnBattle = new Battle(createPredator(), new BattleInventory(createPlayerAnimals(3)), new RandomTestAdapter());
        opponentTurnBattle.startBattle();
        assertFalse(opponentTurnBattle.isPlayerTurn());

        List<Animal> animals = createPlayerAnimals(1);
        assertThrows(NotPlayersTurnException.class, () -> opponentTurnBattle.playerAttack(animals.get(0), StatCategory.SPEED));
    }

    @Test
    void shouldExecuteOpponentAttack() throws Exception {
        Animal opponent = createPredatorWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter(42));
        battle.startBattle();

        // Get the category opponent will use
        StatCategory category = battle.getOpponentSelectedCategory();
        assertNotNull(category);

        // Player selects an animal to defend with (using new delegate method)
        Animal selectedAnimal = battle.getAvailableAnimals().get(0);
        RoundResult result = battle.opponentAttack(selectedAnimal);

        assertNotNull(result);
        assertEquals(selectedAnimal, result.getPlayerAnimal());
        assertEquals(opponent, result.getOpponentAnimal());
        assertEquals(category, result.getCategory());
        assertTrue(battle.isPlayerTurn()); // Turn switches to player
    }

    @Test
    void shouldFinishBattleWhenPlayerReaches3Points() throws Exception {
        Animal opponent = createPredatorWithStats(50, 50, 50, 50, 50);
        List<Animal> playerAnimals = List.of(createAnimalWithStats(100, 100, 100, 100, 100), createAnimalWithStats(100, 100, 100, 100, 100), createAnimalWithStats(100, 100, 100, 100, 100), createAnimalWithStats(100, 100, 100, 100, 100), createAnimalWithStats(100, 100, 100, 100, 100));
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter(42));
        battle.startBattle();

        // Play rounds until battle finishes (using new delegate method)
        int maxRounds = 3;
        int rounds = 0;
        while (!battle.isFinished() && rounds < maxRounds) {
            if (battle.isPlayerTurn()) {
                battle.playerAttack(battle.getAvailableAnimals().get(0), StatCategory.SPEED);
            } else {
                Animal selectedAnimal = battle.getAvailableAnimals().get(0);
                battle.opponentAttack(selectedAnimal);
            }
            rounds++;
        }

        assertTrue(battle.isFinished());
        assertTrue(battle.getPlayerScore() == 3 || battle.getOpponentScore() == 3);
    }

    @Test
    void shouldAllowFleeOnlyWithPreyAndPlayerTurn() throws BattleAlreadyStartedException {
        Animal prey = createPrey();
        BattleInventory battleInventory = new BattleInventory(createPlayerAnimals(3));

        Battle battle = new Battle(prey, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        assertTrue(battle.canFlee());
    }

    @Test
    void shouldNotAllowFleeWithPredator() throws BattleAlreadyStartedException {
        Animal predator = createPredator();
        BattleInventory battleInventory = new BattleInventory(createPlayerAnimals(3));

        Battle battle = new Battle(predator, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        assertFalse(battle.canFlee());
    }

    @Test
    void shouldGetOpponentSelectedCategory() throws Exception {
        Animal opponent = createPredatorWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter(42));
        battle.startBattle();

        StatCategory category = battle.getOpponentSelectedCategory();
        assertNotNull(category);

        // Calling again should return the same category
        StatCategory category2 = battle.getOpponentSelectedCategory();
        assertEquals(category, category2);
    }

    @Test
    void shouldNotGetOpponentSelectedCategoryWhenPlayerTurn() throws BattleAlreadyStartedException {
        Animal opponent = createPrey();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        assertThrows(NotPlayersTurnException.class, battle::getOpponentSelectedCategory);
    }

    @Test
    void shouldClearOpponentSelectedCategoryAfterRound() throws Exception {
        Animal opponent = createPredatorWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = createPlayerAnimals(5);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter(42));
        battle.startBattle();

        // First opponent attack (using delegate method)
        StatCategory category1 = battle.getOpponentSelectedCategory();
        Animal selectedAnimal1 = battle.getAvailableAnimals().get(0);
        battle.opponentAttack(selectedAnimal1);
        assertEquals(StatCategory.SPEED, category1); // Based on the RandomTestAdapter with seed 42, the first category should be SPEED

        // Player attacks (using delegate method)
        Animal selectedAnimal2 = battle.getAvailableAnimals().get(0);
        battle.playerAttack(selectedAnimal2, StatCategory.SPEED);

        // Second opponent attack - should have a potentially different category
        StatCategory category2 = battle.getOpponentSelectedCategory();
        assertNotNull(category2);
        assertEquals(StatCategory.LIFESPAN, category2); // Based on the RandomTestAdapter with seed 42, the second category should be LIFESPAN
    }

    @Test
    void shouldReturnAvailableAnimals() {
        Animal opponent = createPrey();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());

        List<Animal> availableAnimals = battle.getAvailableAnimals();
        assertEquals(3, availableAnimals.size());
        // Verify it's unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> availableAnimals.add(createPrey()));
    }

    @Test
    void shouldReturnAllBattleAnimals() {
        Animal opponent = createPrey();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());

        List<Animal> allAnimals = battle.getAllBattleAnimals();
        assertEquals(3, allAnimals.size());
        // Verify it's unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> allAnimals.add(createPrey()));
    }

    @Test
    void shouldThrowExceptionWhenOpponentAttackWithInvalidAnimal() throws BattleAlreadyStartedException {
        Animal opponent = createPredatorWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        // Try to use an animal that's not in the battle inventory
        Animal invalidAnimal = createAnimalWithStats(50, 50, 50, 50, 50);

        assertThrows(AnimalNotAvailableException.class, () -> battle.opponentAttack(invalidAnimal));
    }

    @Test
    void shouldThrowExceptionWhenOpponentAttackWithNullAnimal() throws BattleAlreadyStartedException {
        Animal opponent = createPredatorWithStats(100, 100, 100, 100, 100);
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);

        Battle battle = new Battle(opponent, battleInventory, new RandomTestAdapter());
        battle.startBattle();

        assertThrows(IllegalArgumentException.class, () -> battle.opponentAttack(null));
    }

    private Battle createTestBattle() {
        Animal opponent = createPrey();
        List<Animal> playerAnimals = createPlayerAnimals(3);
        BattleInventory battleInventory = new BattleInventory(playerAnimals);
        return new Battle(opponent, battleInventory, new RandomTestAdapter());
    }

    private Animal createPredator() {
        return createPredatorWithStats(75, 75, 75, 75, 75);
    }

    private Animal createPrey() {
        Map<StatCategory, Integer> stats = createStatsMap(50, 50, 50, 50, 50);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Rabbit", 1, AnimalType.PREY, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    private Animal createPreyWithStats(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = createStatsMap(speed, length, weight, lifespan, offspring);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Prey", 1, AnimalType.PREY, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    private Animal createPredatorWithStats(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = createStatsMap(speed, length, weight, lifespan, offspring);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Predator", 2, AnimalType.PREDATOR, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    private Animal createAnimalWithStats(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = createStatsMap(speed, length, weight, lifespan, offspring);
        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Test", 1, AnimalType.PREY, animalStats, BiomeType.FOREST);
        return new Animal(species, animalStats);
    }

    private List<Animal> createPlayerAnimals(int count) {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            animals.add(createAnimalWithStats(60, 60, 60, 60, 60));
        }
        return animals;
    }

    private Map<StatCategory, Integer> createStatsMap(int speed, int length, int weight, int lifespan, int offspring) {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.LIFESPAN, lifespan);
        stats.put(StatCategory.OFFSPRING, offspring);
        return stats;
    }
}

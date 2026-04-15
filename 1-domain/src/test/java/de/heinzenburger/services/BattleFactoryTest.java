package de.heinzenburger.services;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.player.Inventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BattleFactoryTest {

    @Test
    void shouldCreateBattleWithPredator() throws InsufficientAnimalsException {
        BattleFactory factory = new BattleFactory(new RandomTestAdapter(42));
        Player player = createPlayerWithAnimals(5);
        Animal predator = createPredator();

        Battle battle = factory.createBattle(player, predator);

        assertNotNull(battle);
        assertEquals(predator, battle.getOpponentAnimal());
        assertFalse(battle.isPlayerTurn()); // Predator attacks first
        assertEquals(3, battle.getAllBattleAnimals().size());
    }

    @Test
    void shouldCreateBattleWithPrey() throws InsufficientAnimalsException {
        BattleFactory factory = new BattleFactory(new RandomTestAdapter(42));
        Player player = createPlayerWithAnimals(5);
        Animal prey = createPrey();

        Battle battle = factory.createBattle(player, prey);

        assertNotNull(battle);
        assertEquals(prey, battle.getOpponentAnimal());
        assertTrue(battle.isPlayerTurn()); // Player can attack first with prey
    }

    @Test
    void shouldSelectThreeRandomAnimals() throws InsufficientAnimalsException {
        BattleFactory factory = new BattleFactory(new RandomTestAdapter(42));
        Player player = createPlayerWithAnimals(5);
        Animal opponent = createPrey();

        Battle battle = factory.createBattle(player, opponent);

        assertEquals(3, battle.getAllBattleAnimals().size());
    }

    @Test
    void shouldSelectAllAnimalsWhenPlayerHasLessThanThree() throws InsufficientAnimalsException {
        BattleFactory factory = new BattleFactory(new RandomTestAdapter(42));
        Player player = createPlayerWithAnimals(2);
        Animal opponent = createPrey();

        Battle battle = factory.createBattle(player, opponent);

        assertEquals(2, battle.getAllBattleAnimals().size());
    }

    @Test
    void shouldThrowExceptionWhenPlayerHasNoAnimals() {
        BattleFactory factory = new BattleFactory(new RandomTestAdapter());
        Inventory emptyInventory = new Inventory(new RandomTestAdapter());
        Player player = new Player(emptyInventory, new Position(0, 0));
        Animal opponent = createPrey();

        assertThrows(InsufficientAnimalsException.class, () -> factory.createBattle(player, opponent));
    }

    private Player createPlayerWithAnimals(int count) {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Player player = new Player(inventory, new Position(0, 0));
        for (int i = 0; i < count; i++) {
            player.addAnimal(createAnimal());
        }
        return player;
    }

    private Animal createAnimal() {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 50);
        stats.put(StatCategory.LENGTH, 50);
        stats.put(StatCategory.WEIGHT, 50);
        stats.put(StatCategory.LIFESPAN, 50);
        stats.put(StatCategory.OFFSPRING, 50);

        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Test", 1, AnimalType.PREY, animalStats, BiomeType.FOREST);
        return new Animal(species, animalStats);
    }

    private Animal createPredator() {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 75);
        stats.put(StatCategory.LENGTH, 75);
        stats.put(StatCategory.WEIGHT, 75);
        stats.put(StatCategory.LIFESPAN, 75);
        stats.put(StatCategory.OFFSPRING, 75);

        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Lion", 2, AnimalType.PREDATOR, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }

    private Animal createPrey() {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 50);
        stats.put(StatCategory.LENGTH, 50);
        stats.put(StatCategory.WEIGHT, 50);
        stats.put(StatCategory.LIFESPAN, 50);
        stats.put(StatCategory.OFFSPRING, 50);

        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Rabbit", 1, AnimalType.PREY, animalStats, BiomeType.GRASSLAND);
        return new Animal(species, animalStats);
    }
}

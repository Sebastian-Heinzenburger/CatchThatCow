package de.heinzenburger.player;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldCreatePlayerWithGeneratedId() {
        Position startPos = new Position(5, 5);
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Player player = new Player(inventory, startPos);

        assertNotNull(player.getId());
    }

    @Test
    void shouldCreatePlayerWithSpecificId() {
        Position startPos = new Position(5, 5);
        Inventory inventory = new Inventory(new RandomTestAdapter());
        PlayerId id = new PlayerId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));

        Player player = new Player(id, inventory, startPos);

        assertEquals(id, player.getId());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        PlayerId id = new PlayerId();
        Inventory inventory1 = new Inventory(new RandomTestAdapter());
        Inventory inventory2 = new Inventory(new RandomTestAdapter());

        Player player1 = new Player(id, inventory1, new Position(0, 0));
        Player player2 = new Player(id, inventory2, new Position(5, 5));

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Position pos = new Position(0, 0);

        Player player1 = new Player(inventory, pos);
        Player player2 = new Player(inventory, pos);

        assertNotEquals(player1, player2);
    }

    @Test
    void shouldCreatePlayerAtStartPosition() {
        Position startPos = new Position(5, 5);
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Player player = new Player(inventory, startPos);

        assertEquals(startPos, player.getCurrentPosition());
        assertNotNull(player.getInventory());
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void shouldAddAnimalToInventory() {
        Player player = createTestPlayer();
        Animal animal = createTestAnimal();

        player.addAnimal(animal);

        assertEquals(1, player.getInventory().size());
        assertTrue(player.getInventory().getAnimals().contains(animal));
    }

    @Test
    void shouldRemoveRandomAnimalFromInventory() throws InsufficientAnimalsException {
        Player player = createTestPlayer();
        Animal animal1 = createTestAnimal();
        Animal animal2 = createTestAnimal();

        player.addAnimal(animal1);
        player.addAnimal(animal2);

        Animal removed = player.removeRandomAnimal();

        assertNotNull(removed);
        assertEquals(1, player.getInventory().size());
        assertFalse(player.getInventory().getAnimals().contains(removed));
    }

    @Test
    void shouldMoveToNewPosition() {
        Position startPos = new Position(0, 0);
        Position newPos = new Position(5, 5);
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Player player = new Player(inventory, startPos);

        player.moveTo(newPos);

        assertEquals(newPos, player.getCurrentPosition());
    }

    @Test
    void shouldThrowExceptionWhenMovingToNullPosition() {
        Player player = createTestPlayer();
        assertThrows(IllegalArgumentException.class, () -> player.moveTo(null));
    }

    private Player createTestPlayer() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        return new Player(inventory, new Position(0, 0));
    }

    private Animal createTestAnimal() {
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        stats.put(StatCategory.SPEED, 10);
        stats.put(StatCategory.LENGTH, 20);
        stats.put(StatCategory.WEIGHT, 30);
        stats.put(StatCategory.LIFESPAN, 40);
        stats.put(StatCategory.OFFSPRING, 50);

        AnimalStats animalStats = new AnimalStats(stats);
        AnimalSpecies species = new AnimalSpecies("Test", 1, AnimalType.PREY, animalStats, BiomeType.FOREST);
        return new Animal(species, animalStats);
    }
}

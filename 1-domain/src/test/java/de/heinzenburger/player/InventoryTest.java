package de.heinzenburger.player;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.AnimalStats;
import de.heinzenburger.shared.AnimalType;
import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.RandomTestAdapter;
import de.heinzenburger.shared.StatCategory;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void shouldStartEmpty() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        assertTrue(inventory.isEmpty());
        assertEquals(0, inventory.size());
    }

    @Test
    void shouldAddAnimal() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Animal animal = createTestAnimal();

        inventory.add(animal);

        assertEquals(1, inventory.size());
        assertFalse(inventory.isEmpty());
        assertTrue(inventory.getAnimals().contains(animal));
    }

    @Test
    void shouldRemoveAnimal() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Animal animal = createTestAnimal();

        inventory.add(animal);
        inventory.remove(animal);

        assertEquals(0, inventory.size());
        assertTrue(inventory.isEmpty());
    }

    @Test
    void shouldRemoveRandomAnimal() throws InsufficientAnimalsException {
        Inventory inventory = new Inventory(new RandomTestAdapter(42));
        Animal animal1 = createTestAnimal();
        Animal animal2 = createTestAnimal();
        Animal animal3 = createTestAnimal();

        inventory.add(animal1);
        inventory.add(animal2);
        inventory.add(animal3);

        Animal removed = inventory.removeRandom();

        assertNotNull(removed);
        assertTrue(animal1.equals(removed) || animal2.equals(removed) || animal3.equals(removed));
        assertEquals(2, inventory.size());
        assertFalse(inventory.getAnimals().contains(removed));
    }

    @Test
    void shouldThrowExceptionWhenRemovingFromEmptyInventory() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        assertThrows(InsufficientAnimalsException.class, inventory::removeRandom);
    }

    @Test
    void shouldSelectRandomAnimalsForBattle() throws InsufficientAnimalsException {
        Inventory inventory = new Inventory(new RandomTestAdapter(42));
        Animal animal1 = createTestAnimal();
        Animal animal2 = createTestAnimal();
        Animal animal3 = createTestAnimal();
        Animal animal4 = createTestAnimal();
        Animal animal5 = createTestAnimal();

        inventory.add(animal1);
        inventory.add(animal2);
        inventory.add(animal3);
        inventory.add(animal4);
        inventory.add(animal5);

        List<Animal> battleAnimals = inventory.selectRandomForBattle(3);

        assertEquals(3, battleAnimals.size());
        // Original inventory should remain unchanged
        assertEquals(5, inventory.size());
    }

    @Test
    void shouldSelectAllAnimalsWhenCountExceedsSize() throws InsufficientAnimalsException {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Animal animal1 = createTestAnimal();
        Animal animal2 = createTestAnimal();

        inventory.add(animal1);
        inventory.add(animal2);

        List<Animal> battleAnimals = inventory.selectRandomForBattle(5);

        assertEquals(2, battleAnimals.size());
    }

    @Test
    void shouldThrowExceptionWhenSelectingFromEmptyInventory() {
        Inventory inventory = new Inventory(new RandomTestAdapter());

        assertThrows(InsufficientAnimalsException.class, () -> inventory.selectRandomForBattle(3));
    }

    @Test
    void shouldReturnUnmodifiableList() {
        Inventory inventory = new Inventory(new RandomTestAdapter());
        Animal animal = createTestAnimal();
        inventory.add(animal);

        List<Animal> animals = inventory.getAnimals();

        assertThrows(UnsupportedOperationException.class, () -> animals.add(createTestAnimal()));
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

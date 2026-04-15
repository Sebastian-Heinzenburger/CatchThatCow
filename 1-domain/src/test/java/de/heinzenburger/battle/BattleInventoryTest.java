package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BattleInventoryTest {

    @Test
    void shouldCreateBattleInventory() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);

        assertEquals(3, inventory.getAllAnimals().size());
        assertEquals(3, inventory.getAvailableAnimals().size());
        assertTrue(inventory.hasAvailableAnimals());
    }

    @Test
    void shouldMarkAnimalAsUsed() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);
        Animal animal = animals.get(0);

        inventory.use(animal);

        assertEquals(2, inventory.getAvailableAnimals().size());
        assertFalse(inventory.getAvailableAnimals().contains(animal));
    }

    @Test
    void shouldThrowExceptionWhenUsingAnimalNotInInventory() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);
        Animal outsideAnimal = createAnimal();

        assertThrows(IllegalArgumentException.class, () -> inventory.use(outsideAnimal));
    }

    @Test
    void shouldThrowExceptionWhenUsingAnimalTwice() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);
        Animal animal = animals.get(0);

        inventory.use(animal);

        assertThrows(IllegalStateException.class, () -> inventory.use(animal));
    }

    @Test
    void shouldHaveNoAvailableAnimalsWhenAllUsed() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);

        inventory.use(animals.get(0));
        inventory.use(animals.get(1));
        inventory.use(animals.get(2));

        assertFalse(inventory.hasAvailableAnimals());
        assertEquals(0, inventory.getAvailableAnimals().size());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        List<Animal> animals = createAnimals(3);
        BattleInventory inventory = new BattleInventory(animals);

        List<Animal> available = inventory.getAvailableAnimals();

        assertThrows(UnsupportedOperationException.class, () -> available.add(createAnimal()));
    }

    private List<Animal> createAnimals(int count) {
        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            animals.add(createAnimal());
        }
        return animals;
    }

    private Animal createAnimal() {
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

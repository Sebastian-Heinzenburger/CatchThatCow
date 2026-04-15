package de.heinzenburger.player;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory {
    private final List<Animal> animals;
    private final RandomNumberGenerator random;

    public Inventory(RandomNumberGenerator random) {
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        this.animals = new ArrayList<>();
        this.random = random;
    }

    public void add(Animal animal) {
        if (animal == null) throw new IllegalArgumentException("Animal cannot be null");
        animals.add(animal);
    }

    public void remove(Animal animal) {
        if (animal == null) throw new IllegalArgumentException("Animal cannot be null");
        animals.remove(animal);
    }

    public Animal removeRandom() throws InsufficientAnimalsException {
        if (animals.isEmpty()) throw new InsufficientAnimalsException(1, 0);
        int index = random.nextInt(animals.size());
        return animals.remove(index);
    }

    public List<Animal> selectRandomForBattle(int count) throws InsufficientAnimalsException {
        if (count < 0) throw new IllegalArgumentException("Count cannot be negative");
        if (animals.isEmpty()) throw new InsufficientAnimalsException(count, 0);
        int selectCount = Math.min(count, animals.size());

        List<Animal> allAnimals = new ArrayList<>(animals);
        // Fisher-Yates shuffle algorithm
        for (int i = allAnimals.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Animal temp = allAnimals.get(i);
            allAnimals.set(i, allAnimals.get(j));
            allAnimals.set(j, temp);
        }
        return new ArrayList<>(allAnimals.subList(0, selectCount));
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public int size() {
        return animals.size();
    }

    public boolean isEmpty() {
        return animals.isEmpty();
    }
}

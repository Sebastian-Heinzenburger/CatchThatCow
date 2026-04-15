package de.heinzenburger.battle;

import de.heinzenburger.animal.Animal;

import java.util.*;

public final class BattleInventory {
    private final List<Animal> animals;
    private final Set<Animal> usedAnimals;

    public BattleInventory(List<Animal> animals) {
        if (animals == null) {
            throw new IllegalArgumentException("Animals cannot be null");
        }
        this.animals = new ArrayList<>(animals);
        this.usedAnimals = new HashSet<>();
    }

    public List<Animal> getAvailableAnimals() {
        List<Animal> available = new ArrayList<>();
        for (Animal animal : animals) {
            if (!usedAnimals.contains(animal)) {
                available.add(animal);
            }
        }
        return Collections.unmodifiableList(available);
    }

    public void use(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null");
        }
        if (!animals.contains(animal)) {
            throw new IllegalArgumentException("Animal is not in battle inventory");
        }
        if (usedAnimals.contains(animal)) {
            throw new IllegalStateException("Animal has already been used in this battle");
        }
        usedAnimals.add(animal);
    }

    public boolean hasAvailableAnimals() {
        return usedAnimals.size() < animals.size();
    }

    public List<Animal> getAllAnimals() {
        return Collections.unmodifiableList(animals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleInventory that = (BattleInventory) o;
        return Objects.equals(animals, that.animals) &&
                Objects.equals(usedAnimals, that.usedAnimals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animals, usedAnimals);
    }
}

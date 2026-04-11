package de.heinzenburger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Player {
    private Position position;
    private final List<Animal> inventory;

    public Player(Position startPosition) {
        this.position = startPosition;
        this.inventory = new ArrayList<>();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<Animal> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public void addAnimal(Animal animal) {
        inventory.add(animal);
    }

    public void removeAnimal(Animal animal) {
        inventory.remove(animal);
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public List<Animal> getRandomBattleInventory(int size) {
        if (inventory.size() < size) {
            throw new IllegalStateException("Nicht genug Tiere im Inventar! Benötigt: " + size + ", Vorhanden: " + inventory.size());
        }

        List<Animal> shuffled = new ArrayList<>(inventory);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, size);
    }
}

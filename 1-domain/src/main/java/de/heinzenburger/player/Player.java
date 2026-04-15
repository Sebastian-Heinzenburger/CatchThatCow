package de.heinzenburger.player;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.shared.Position;

public class Player {
    private final Inventory inventory;
    private Position currentPosition;

    public Player(Inventory inventory, Position currentPosition) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        if (currentPosition == null) {
            throw new IllegalArgumentException("Current position cannot be null");
        }

        this.inventory = inventory;
        this.currentPosition = currentPosition;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void addAnimal(Animal animal) {
        inventory.add(animal);
    }

    public Animal removeRandomAnimal() {
        return inventory.removeRandom();
    }

    public void moveTo(Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("New position cannot be null");
        }
        this.currentPosition = newPosition;
    }

    @Override
    public String toString() {
        return "Player{" +
                "position=" + currentPosition +
                ", animals=" + inventory.size() +
                '}';
    }
}

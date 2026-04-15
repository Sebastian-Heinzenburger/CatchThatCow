package de.heinzenburger.player;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.Position;

import java.util.Objects;

public class Player {
    private final PlayerId id;
    private final Inventory inventory;
    private Position currentPosition;

    public Player(Inventory inventory, Position currentPosition) {
        this(new PlayerId(), inventory, currentPosition);
    }

    public Player(PlayerId id, Inventory inventory, Position currentPosition) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        if (currentPosition == null) {
            throw new IllegalArgumentException("Current position cannot be null");
        }

        this.id = id;
        this.inventory = inventory;
        this.currentPosition = currentPosition;
    }

    public PlayerId getId() {
        return id;
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

    public Animal removeRandomAnimal() throws InsufficientAnimalsException {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

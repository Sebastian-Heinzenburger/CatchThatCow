package de.heinzenburger.services;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.battle.Battle;
import de.heinzenburger.battle.BattleInventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.exception.InsufficientAnimalsException;
import de.heinzenburger.shared.RandomNumberGenerator;

import java.util.List;

public class BattleFactory {
    private static final int BATTLE_INVENTORY_SIZE = 3;
    private final RandomNumberGenerator random;

    public BattleFactory(RandomNumberGenerator random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        this.random = random;
    }

    public Battle createBattle(Player player, Animal opponent) throws InsufficientAnimalsException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (opponent == null) {
            throw new IllegalArgumentException("Opponent cannot be null");
        }

        // Select 3 random animals from player's inventory for the battle
        List<Animal> selectedAnimals = player.getInventory()
                .selectRandomForBattle(BATTLE_INVENTORY_SIZE);

        BattleInventory battleInventory = new BattleInventory(selectedAnimals);

        return new Battle(opponent, battleInventory, random);
    }
}

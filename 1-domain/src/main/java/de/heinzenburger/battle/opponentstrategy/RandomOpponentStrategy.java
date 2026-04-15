package de.heinzenburger.battle.opponentstrategy;

import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.shared.StatCategory;

/**
 * Opponent strategy that randomly selects a stat category.
 * Caches the selection until reset() is called.
 */
public class RandomOpponentStrategy implements OpponentStrategy {
    private final RandomNumberGenerator random;
    private StatCategory selectedCategory;

    public RandomOpponentStrategy(RandomNumberGenerator random) {
        if (random == null) {
            throw new IllegalArgumentException("Random number generator cannot be null");
        }
        this.random = random;
    }

    @Override
    public StatCategory selectCategory() {
        if (selectedCategory == null) {
            StatCategory[] categories = StatCategory.values();
            selectedCategory = categories[random.nextInt(categories.length)];
        }
        return selectedCategory;
    }

    @Override
    public void reset() {
        selectedCategory = null;
    }
}

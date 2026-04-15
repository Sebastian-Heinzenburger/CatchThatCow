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
        if (random == null) throw new IllegalArgumentException("Random number generator cannot be null");
        this.random = random;
    }

    @Override
    public StatCategory selectCategory() {
        if (selectedCategory != null) return selectedCategory;
        StatCategory[] categories = StatCategory.values();
        return selectedCategory = random.choice(categories);
    }

    @Override
    public void reset() {
        selectedCategory = null;
    }
}

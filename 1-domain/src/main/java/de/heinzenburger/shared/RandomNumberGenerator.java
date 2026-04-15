package de.heinzenburger.shared;

import java.util.List;

/**
 * Domain abstraction for random number generation.
 * Allows deterministic testing and potential future enhancements
 * like replay systems or difficulty adjustments.
 */
public interface RandomNumberGenerator {
    /**
     * Returns a random integer in range [0, bound)
     * @param bound the upper bound (exclusive). Must be positive.
     * @return random integer in [0, bound)
     */
    int nextInt(int bound);

    /**
     * Returns a random double in range [0.0, 1.0)
     * @return random double in [0.0, 1.0)
     */
    double nextDouble();

    <T> T choice(List<T> items);
    <T> T choice(T[] items);
}

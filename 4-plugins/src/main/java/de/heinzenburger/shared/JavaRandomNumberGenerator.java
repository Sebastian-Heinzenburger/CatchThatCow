package de.heinzenburger.shared;

import java.util.List;
import java.util.Random;

/**
 * Adapter that bridges java.util.Random to domain's RandomNumberGenerator interface.
 * Lives in the adapters layer per clean architecture principles.
 * Provides constructors for production use and deterministic testing.
 */
public final class JavaRandomNumberGenerator implements RandomNumberGenerator {
    private final Random random;

    public JavaRandomNumberGenerator() {
        this(new Random());
    }

    public JavaRandomNumberGenerator(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        this.random = random;
    }

    /**
     * Constructor for deterministic testing with seed.
     */
    public JavaRandomNumberGenerator(long seed) {
        this(new Random(seed));
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public <T> T choice(List<T> items) {
        return items.get(random.nextInt(items.size()));
    }

    @Override
    public <T> T choice(T[] items) {
        return items[random.nextInt(items.length)];
    }
}

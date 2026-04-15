package de.heinzenburger.shared;

import java.util.Random;

/**
 * Adapter that bridges java.util.Random to domain's RandomNumberGenerator interface.
 * Lives in the adapters layer per clean architecture principles.
 * Provides constructors for production use and deterministic testing.
 */
public final class JavaRandomAdapter implements RandomNumberGenerator {
    private final Random random;

    public JavaRandomAdapter() {
        this(new Random());
    }

    public JavaRandomAdapter(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        this.random = random;
    }

    /**
     * Constructor for deterministic testing with seed.
     */
    public JavaRandomAdapter(long seed) {
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
}

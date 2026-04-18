package de.heinzenburger.world;

import de.heinzenburger.persistence.GameStatePersistence;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * File-based implementation of WorldRepository.
 * Persists world state to a text file using GameStatePersistence.
 */
public final class FileWorldRepository implements WorldRepository {

    private static final String DEFAULT_FILENAME = "world.txt";

    private final Path filePath;

    public FileWorldRepository() {
        this(Path.of("."));
    }

    public FileWorldRepository(Path directory) {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");
        this.filePath = directory.resolve(DEFAULT_FILENAME);
    }

    @Override
    public void save(World world) {
        if (world == null) throw new IllegalArgumentException("World cannot be null");

        try {
            GameStatePersistence.saveWorld(world, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save world state", e);
        }
    }

    @Override
    public Optional<World> load() {
        return GameStatePersistence.loadWorld(filePath);
    }
}

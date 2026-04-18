package de.heinzenburger.player;

import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.persistence.GameStatePersistence;
import de.heinzenburger.shared.RandomNumberGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * File-based implementation of PlayerRepository.
 * Persists player state to a text file using GameStatePersistence.
 */
public final class FilePlayerRepository implements PlayerRepository {

    private static final String DEFAULT_FILENAME = "player.txt";

    private final Path filePath;
    private final RandomNumberGenerator random;
    private final AnimalSpeciesRepository speciesRepository;

    public FilePlayerRepository(RandomNumberGenerator random, AnimalSpeciesRepository speciesRepository) {
        this(Path.of(DEFAULT_FILENAME), random, speciesRepository);
    }

    public FilePlayerRepository(Path directory, RandomNumberGenerator random, AnimalSpeciesRepository speciesRepository) {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        if (speciesRepository == null) throw new IllegalArgumentException("Species repository cannot be null");

        this.filePath = directory.resolve(DEFAULT_FILENAME);
        this.random = random;
        this.speciesRepository = speciesRepository;
    }

    @Override
    public void save(Player player) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");

        try {
            GameStatePersistence.savePlayer(player, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save player state", e);
        }
    }

    @Override
    public Optional<Player> load() {
        return GameStatePersistence.loadPlayer(filePath, random, speciesRepository);
    }
}

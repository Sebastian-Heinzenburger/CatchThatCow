package de.heinzenburger.game;

import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.persistence.GameStatePersistence;
import de.heinzenburger.player.Player;
import de.heinzenburger.shared.RandomNumberGenerator;
import de.heinzenburger.world.World;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * File-based implementation of GameStateRepository.
 * Saves player and world atomically to a single directory.
 */
public final class FileGameStateRepository implements GameStateRepository {

    private static final String PLAYER_FILENAME = "player.txt";
    private static final String WORLD_FILENAME = "world.txt";

    private final Path playerFilePath;
    private final Path worldFilePath;
    private final RandomNumberGenerator random;
    private final AnimalSpeciesRepository speciesRepository;

    public FileGameStateRepository(RandomNumberGenerator random, AnimalSpeciesRepository speciesRepository) {
        this(Path.of("."), random, speciesRepository);
    }

    public FileGameStateRepository(Path directory, RandomNumberGenerator random, AnimalSpeciesRepository speciesRepository) {
        if (directory == null) throw new IllegalArgumentException("Directory cannot be null");
        if (random == null) throw new IllegalArgumentException("Random cannot be null");
        if (speciesRepository == null) throw new IllegalArgumentException("Species repository cannot be null");

        this.playerFilePath = directory.resolve(PLAYER_FILENAME);
        this.worldFilePath = directory.resolve(WORLD_FILENAME);
        this.random = random;
        this.speciesRepository = speciesRepository;
    }

    @Override
    public Optional<GameState> load() {
        Optional<Player> player = GameStatePersistence.loadPlayer(playerFilePath, random, speciesRepository);
        Optional<World> world = GameStatePersistence.loadWorld(worldFilePath);

        if (player.isPresent() && world.isPresent()) {
            return Optional.of(new GameState(player.get(), world.get()));
        }
        return Optional.empty();
    }

    @Override
    public void save(Player player, World world) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (world == null) throw new IllegalArgumentException("World cannot be null");

        try {
            GameStatePersistence.savePlayer(player, playerFilePath);
            GameStatePersistence.saveWorld(world, worldFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save game state", e);
        }
    }
}

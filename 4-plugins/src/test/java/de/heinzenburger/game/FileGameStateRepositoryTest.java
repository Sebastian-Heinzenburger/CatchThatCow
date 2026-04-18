package de.heinzenburger.game;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.HardcodedAnimalSpeciesRepository;
import de.heinzenburger.player.Inventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.shared.*;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileGameStateRepositoryTest {

    @TempDir
    Path tempDir;

    private FileGameStateRepository repository;
    private TestRandomAdapter random;
    private HardcodedAnimalSpeciesRepository speciesRepo;

    @BeforeEach
    void setUp() {
        random = new TestRandomAdapter();
        speciesRepo = new HardcodedAnimalSpeciesRepository();
        repository = new FileGameStateRepository(tempDir, random, speciesRepo);
    }

    @Test
    void saveAndLoad_preservesGameState() {
        Player player = createTestPlayer();
        World world = createTestWorld();

        repository.save(player, world);
        Optional<GameState> loaded = repository.load();

        assertTrue(loaded.isPresent());
        GameState state = loaded.get();
        assertEquals(player.getId(), state.player().getId());
        assertEquals(world.getId(), state.world().getId());
    }

    @Test
    void load_returnsEmptyWhenNoSavedState() {
        Optional<GameState> result = repository.load();

        assertTrue(result.isEmpty());
    }

    @Test
    void save_throwsExceptionForNullPlayer() {
        World world = createTestWorld();

        assertThrows(IllegalArgumentException.class, () -> repository.save(null, world));
    }

    @Test
    void save_throwsExceptionForNullWorld() {
        Player player = createTestPlayer();

        assertThrows(IllegalArgumentException.class, () -> repository.save(player, null));
    }

    @Test
    void constructor_throwsExceptionForNullDirectory() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileGameStateRepository(null, random, speciesRepo));
    }

    @Test
    void constructor_throwsExceptionForNullRandom() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileGameStateRepository(tempDir, null, speciesRepo));
    }

    @Test
    void constructor_throwsExceptionForNullSpeciesRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileGameStateRepository(tempDir, random, null));
    }

    private Player createTestPlayer() {
        AnimalSpecies rabbit = speciesRepo.findByHabitat(BiomeType.GRASSLAND).stream()
                .filter(s -> s.name().equals("Rabbit"))
                .findFirst().orElseThrow();
        Inventory inventory = new Inventory(random);
        inventory.add(rabbit.generateAnimalWithSlightStatVariation(random));
        return new Player(inventory, new Position(2, 2));
    }

    private World createTestWorld() {
        Map<Position, Biome> biomes = new HashMap<>();
        Position start = new Position(2, 2);
        biomes.put(start, new Biome(start, BiomeType.GRASSLAND, 0));
        return new World(5, start, biomes);
    }

    private static class TestRandomAdapter implements RandomNumberGenerator {
        @Override
        public int nextInt(int bound) {
            return 0;
        }

        @Override
        public double nextDouble() {
            return 0.5;
        }

        @Override
        public <T> T choice(java.util.List<T> items) {
            return items.get(0);
        }

        @Override
        public <T> T choice(T[] items) {
            return items[0];
        }
    }
}

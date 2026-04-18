package de.heinzenburger.player;

import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.HardcodedAnimalSpeciesRepository;
import de.heinzenburger.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FilePlayerRepositoryTest {

    @TempDir
    Path tempDir;

    private FilePlayerRepository repository;
    private TestRandomAdapter random;
    private HardcodedAnimalSpeciesRepository speciesRepo;

    @BeforeEach
    void setUp() {
        random = new TestRandomAdapter();
        speciesRepo = new HardcodedAnimalSpeciesRepository();
        repository = new FilePlayerRepository(tempDir, random, speciesRepo);
    }

    @Test
    void saveAndLoad_preservesPlayer() {
        Player original = createTestPlayer();

        repository.save(original);
        Optional<Player> loaded = repository.load();

        assertTrue(loaded.isPresent());
        assertEquals(original.getId(), loaded.get().getId());
        assertEquals(original.getCurrentPosition(), loaded.get().getCurrentPosition());
    }

    @Test
    void load_returnsEmptyWhenNoSavedPlayer() {
        Optional<Player> result = repository.load();

        assertTrue(result.isEmpty());
    }

    @Test
    void save_throwsExceptionForNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void constructor_throwsExceptionForNullDirectory() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilePlayerRepository(null, random, speciesRepo));
    }

    @Test
    void constructor_throwsExceptionForNullRandom() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilePlayerRepository(tempDir, null, speciesRepo));
    }

    @Test
    void constructor_throwsExceptionForNullSpeciesRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilePlayerRepository(tempDir, random, null));
    }

    private Player createTestPlayer() {
        AnimalSpecies rabbit = speciesRepo.findByHabitat(BiomeType.GRASSLAND).stream()
                .filter(s -> s.name().equals("Rabbit"))
                .findFirst().orElseThrow();
        Inventory inventory = new Inventory(random);
        inventory.add(rabbit.generateAnimalWithSlightStatVariation(random));
        return new Player(inventory, new Position(3, 5));
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

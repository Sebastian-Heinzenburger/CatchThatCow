package de.heinzenburger.world;

import de.heinzenburger.shared.BiomeType;
import de.heinzenburger.shared.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileWorldRepositoryTest {

    @TempDir
    Path tempDir;

    private FileWorldRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FileWorldRepository(tempDir);
    }

    @Test
    void saveAndLoad_preservesWorld() {
        World original = createTestWorld();

        repository.save(original);
        Optional<World> loaded = repository.load();

        assertTrue(loaded.isPresent());
        assertEquals(original.getId(), loaded.get().getId());
        assertEquals(original.getSize(), loaded.get().getSize());
        assertEquals(original.getStartPosition(), loaded.get().getStartPosition());
    }

    @Test
    void load_returnsEmptyWhenNoSavedWorld() {
        Optional<World> result = repository.load();

        assertTrue(result.isEmpty());
    }

    @Test
    void save_throwsExceptionForNullWorld() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void constructor_throwsExceptionForNullDirectory() {
        assertThrows(IllegalArgumentException.class, () -> new FileWorldRepository(null));
    }

    @Test
    void saveAndLoad_preservesAllBiomes() {
        Map<Position, Biome> biomes = new HashMap<>();
        Position start = new Position(2, 2);
        biomes.put(start, new Biome(start, BiomeType.GRASSLAND, 0));
        biomes.put(new Position(2, 3), new Biome(new Position(2, 3), BiomeType.FOREST, 1));
        biomes.put(new Position(3, 2), new Biome(new Position(3, 2), BiomeType.DESERT, 1));
        biomes.put(new Position(1, 2), new Biome(new Position(1, 2), BiomeType.OCEAN, 1));
        World original = new World(5, start, biomes);

        repository.save(original);
        Optional<World> loaded = repository.load();

        assertTrue(loaded.isPresent());
        World world = loaded.get();
        assertEquals(4, world.getAllBiomes().size());
        assertEquals(BiomeType.GRASSLAND, world.getBiomeAt(start).type());
        assertEquals(BiomeType.FOREST, world.getBiomeAt(new Position(2, 3)).type());
        assertEquals(BiomeType.DESERT, world.getBiomeAt(new Position(3, 2)).type());
        assertEquals(BiomeType.OCEAN, world.getBiomeAt(new Position(1, 2)).type());
    }

    private World createTestWorld() {
        Map<Position, Biome> biomes = new HashMap<>();
        Position start = new Position(2, 2);
        biomes.put(start, new Biome(start, BiomeType.GRASSLAND, 0));
        return new World(5, start, biomes);
    }
}

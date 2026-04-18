package de.heinzenburger.persistence;

import de.heinzenburger.animal.Animal;
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameStatePersistenceTest {

    @TempDir
    Path tempDir;

    private TestRandomAdapter random;
    private HardcodedAnimalSpeciesRepository speciesRepo;

    @BeforeEach
    void setUp() {
        random = new TestRandomAdapter();
        speciesRepo = new HardcodedAnimalSpeciesRepository();
    }

    @Test
    void saveAndLoadPlayer_preservesPlayerState() throws IOException {
        AnimalSpecies rabbitSpecies = speciesRepo.findByHabitat(BiomeType.GRASSLAND).stream()
                .filter(s -> s.name().equals("Rabbit"))
                .findFirst().orElseThrow();
        Animal rabbit = rabbitSpecies.generateAnimalWithSlightStatVariation(random);

        Inventory inventory = new Inventory(random);
        inventory.add(rabbit);
        Player original = new Player(inventory, new Position(3, 5));

        Path playerFile = tempDir.resolve("player.txt");
        GameStatePersistence.savePlayer(original, playerFile);

        Optional<Player> loaded = GameStatePersistence.loadPlayer(playerFile, random, speciesRepo);

        assertTrue(loaded.isPresent());
        Player player = loaded.get();
        assertEquals(original.getId(), player.getId());
        assertEquals(original.getCurrentPosition(), player.getCurrentPosition());
        assertEquals(1, player.getInventory().size());
        assertEquals(rabbit.getId(), player.getInventory().getAnimals().get(0).getId());
    }

    @Test
    void loadPlayer_returnsEmptyWhenFileDoesNotExist() {
        Path nonExistent = tempDir.resolve("nonexistent.txt");

        Optional<Player> result = GameStatePersistence.loadPlayer(nonExistent, random, speciesRepo);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndLoadWorld_preservesWorldState() throws IOException {
        Map<Position, Biome> biomes = new HashMap<>();
        Position start = new Position(2, 2);
        biomes.put(start, new Biome(start, BiomeType.GRASSLAND, 0));
        biomes.put(new Position(2, 3), new Biome(new Position(2, 3), BiomeType.FOREST, 1));
        biomes.put(new Position(3, 2), new Biome(new Position(3, 2), BiomeType.DESERT, 1));
        World original = new World(5, start, biomes);

        Path worldFile = tempDir.resolve("world.txt");
        GameStatePersistence.saveWorld(original, worldFile);

        Optional<World> loaded = GameStatePersistence.loadWorld(worldFile);

        assertTrue(loaded.isPresent());
        World world = loaded.get();
        assertEquals(original.getId(), world.getId());
        assertEquals(original.getSize(), world.getSize());
        assertEquals(original.getStartPosition(), world.getStartPosition());
        assertEquals(original.getAllBiomes().size(), world.getAllBiomes().size());

        Biome loadedBiome = world.getBiomeAt(new Position(2, 3));
        assertEquals(BiomeType.FOREST, loadedBiome.type());
        assertEquals(1, loadedBiome.distanceFromStart());
    }

    @Test
    void loadWorld_returnsEmptyWhenFileDoesNotExist() {
        Path nonExistent = tempDir.resolve("nonexistent.txt");

        Optional<World> result = GameStatePersistence.loadWorld(nonExistent);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndLoadPlayer_preservesMultipleAnimals() throws IOException {
        Inventory inventory = new Inventory(random);

        AnimalSpecies rabbit = speciesRepo.findByHabitat(BiomeType.GRASSLAND).get(0);
        AnimalSpecies fox = speciesRepo.findByHabitat(BiomeType.GRASSLAND).get(1);
        AnimalSpecies deer = speciesRepo.findByHabitat(BiomeType.GRASSLAND).get(2);

        inventory.add(rabbit.generateAnimalWithSlightStatVariation(random));
        inventory.add(fox.generateAnimalWithSlightStatVariation(random));
        inventory.add(deer.generateAnimalWithSlightStatVariation(random));

        Player original = new Player(inventory, new Position(0, 0));

        Path playerFile = tempDir.resolve("player.txt");
        GameStatePersistence.savePlayer(original, playerFile);

        Optional<Player> loaded = GameStatePersistence.loadPlayer(playerFile, random, speciesRepo);

        assertTrue(loaded.isPresent());
        assertEquals(3, loaded.get().getInventory().size());
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

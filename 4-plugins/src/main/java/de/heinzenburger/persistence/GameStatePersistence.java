package de.heinzenburger.persistence;

import de.heinzenburger.animal.Animal;
import de.heinzenburger.animal.AnimalId;
import de.heinzenburger.animal.AnimalSpecies;
import de.heinzenburger.animal.AnimalSpeciesRepository;
import de.heinzenburger.player.Inventory;
import de.heinzenburger.player.Player;
import de.heinzenburger.player.PlayerId;
import de.heinzenburger.shared.*;
import de.heinzenburger.world.Biome;
import de.heinzenburger.world.World;
import de.heinzenburger.world.WorldId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Utility class for serializing and deserializing game state to text files.
 * Uses Properties-like key=value format for persistence without third-party libraries.
 */
public final class GameStatePersistence {

    private GameStatePersistence() {
    }

    public static void savePlayer(Player player, Path filePath) throws IOException {
        Properties props = new Properties();

        props.setProperty("player.id", player.getId().getValue().toString());
        props.setProperty("player.position.x", String.valueOf(player.getCurrentPosition().x()));
        props.setProperty("player.position.y", String.valueOf(player.getCurrentPosition().y()));

        var animals = player.getInventory().getAnimals();
        props.setProperty("player.animals.count", String.valueOf(animals.size()));

        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            String prefix = "player.animal." + i;
            props.setProperty(prefix + ".id", animal.getId().getValue().toString());
            props.setProperty(prefix + ".species", animal.getSpecies().name());
            for (StatCategory cat : StatCategory.values()) {
                props.setProperty(prefix + ".stats." + cat.name(), String.valueOf(animal.getStat(cat)));
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            props.store(writer, "Player Game State");
        }
    }

    public static Optional<Player> loadPlayer(Path filePath, RandomNumberGenerator random,
                                              AnimalSpeciesRepository speciesRepo) {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        Properties props = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            props.load(reader);
        } catch (IOException e) {
            return Optional.empty();
        }

        try {
            PlayerId playerId = new PlayerId(UUID.fromString(props.getProperty("player.id")));
            int posX = Integer.parseInt(props.getProperty("player.position.x"));
            int posY = Integer.parseInt(props.getProperty("player.position.y"));
            Position position = new Position(posX, posY);

            Inventory inventory = new Inventory(random);
            int animalCount = Integer.parseInt(props.getProperty("player.animals.count"));

            var allSpecies = speciesRepo.findAll();

            for (int i = 0; i < animalCount; i++) {
                String prefix = "player.animal." + i;
                AnimalId animalId = new AnimalId(UUID.fromString(props.getProperty(prefix + ".id")));
                String speciesName = props.getProperty(prefix + ".species");

                AnimalSpecies species = allSpecies.stream()
                        .filter(s -> s.name().equals(speciesName))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Unknown species: " + speciesName));

                Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
                for (StatCategory cat : StatCategory.values()) {
                    stats.put(cat, Integer.parseInt(props.getProperty(prefix + ".stats." + cat.name())));
                }

                Animal animal = new Animal(animalId, species, new AnimalStats(stats));
                inventory.add(animal);
            }

            return Optional.of(new Player(playerId, inventory, position));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static void saveWorld(World world, Path filePath) throws IOException {
        Properties props = new Properties();

        props.setProperty("world.id", world.getId().getValue().toString());
        props.setProperty("world.size", String.valueOf(world.getSize()));
        props.setProperty("world.startPosition.x", String.valueOf(world.getStartPosition().x()));
        props.setProperty("world.startPosition.y", String.valueOf(world.getStartPosition().y()));

        var biomes = world.getAllBiomes();
        props.setProperty("world.biomes.count", String.valueOf(biomes.size()));

        int idx = 0;
        for (var entry : biomes.entrySet()) {
            Position pos = entry.getKey();
            Biome biome = entry.getValue();
            String prefix = "world.biome." + idx;
            props.setProperty(prefix + ".x", String.valueOf(pos.x()));
            props.setProperty(prefix + ".y", String.valueOf(pos.y()));
            props.setProperty(prefix + ".type", biome.type().name());
            props.setProperty(prefix + ".distance", String.valueOf(biome.distanceFromStart()));
            idx++;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            props.store(writer, "World Game State");
        }
    }

    public static Optional<World> loadWorld(Path filePath) {
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        Properties props = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            props.load(reader);
        } catch (IOException e) {
            return Optional.empty();
        }

        try {
            WorldId worldId = new WorldId(UUID.fromString(props.getProperty("world.id")));
            int size = Integer.parseInt(props.getProperty("world.size"));
            int startX = Integer.parseInt(props.getProperty("world.startPosition.x"));
            int startY = Integer.parseInt(props.getProperty("world.startPosition.y"));
            Position startPosition = new Position(startX, startY);

            int biomeCount = Integer.parseInt(props.getProperty("world.biomes.count"));
            Map<Position, Biome> biomes = new HashMap<>();

            for (int i = 0; i < biomeCount; i++) {
                String prefix = "world.biome." + i;
                int x = Integer.parseInt(props.getProperty(prefix + ".x"));
                int y = Integer.parseInt(props.getProperty(prefix + ".y"));
                BiomeType type = BiomeType.valueOf(props.getProperty(prefix + ".type"));
                int distance = Integer.parseInt(props.getProperty(prefix + ".distance"));

                Position pos = new Position(x, y);
                biomes.put(pos, new Biome(pos, type, distance));
            }

            return Optional.of(new World(worldId, size, startPosition, biomes));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

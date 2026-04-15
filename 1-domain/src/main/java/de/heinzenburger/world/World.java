package de.heinzenburger.world;

import de.heinzenburger.shared.Direction;
import de.heinzenburger.shared.Position;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class World {
    private final WorldId id;
    private final int size;
    private final Position startPosition;
    private final Map<Position, Biome> biomes;

    public World(int size, Position startPosition, Map<Position, Biome> biomes) {
        this(new WorldId(), size, startPosition, biomes);
    }

    public World(WorldId id, int size, Position startPosition, Map<Position, Biome> biomes) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        if (size <= 0) throw new IllegalArgumentException("Size must be positive");
        if (startPosition == null) throw new IllegalArgumentException("Start position cannot be null");
        if (biomes == null || biomes.isEmpty()) throw new IllegalArgumentException("Biomes cannot be null or empty");

        this.id = id;
        this.size = size;
        this.startPosition = startPosition;
        this.biomes = new HashMap<>(biomes);
    }

    public WorldId getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Biome getBiomeAt(Position position) {
        return biomes.get(position);
    }

    public Map<Direction, Biome> getAdjacentBiomes(Position position) {
        Map<Direction, Biome> adjacent = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            Position neighbour = position.neighbour(direction);
            if (biomes.containsKey(neighbour)) adjacent.put(direction, biomes.get(neighbour));
        }
        return adjacent;
    }

    public boolean isValidPosition(Position position) {
        return biomes.containsKey(position);
    }

    public Map<Position, Biome> getAllBiomes() {
        return Collections.unmodifiableMap(biomes);
    }

    @Override
    public String toString() {
        return "World{" + "size=" + size + ", biomes=" + biomes.size() + '}';
    }

}

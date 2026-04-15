package de.heinzenburger.world;

import de.heinzenburger.shared.Position;

import java.util.*;

public class World {
    private final WorldId id;
    private final int size;
    private final Position startPosition;
    private final Map<Position, Biome> biomes;

    public World(int size, Position startPosition, Map<Position, Biome> biomes) {
        this(new WorldId(), size, startPosition, biomes);
    }

    public World(WorldId id, int size, Position startPosition, Map<Position, Biome> biomes) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        if (startPosition == null) {
            throw new IllegalArgumentException("Start position cannot be null");
        }
        if (biomes == null || biomes.isEmpty()) {
            throw new IllegalArgumentException("Biomes cannot be null or empty");
        }

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

        Position north = new Position(position.getX(), position.getY() - 1);
        Position east = new Position(position.getX() + 1, position.getY());
        Position south = new Position(position.getX(), position.getY() + 1);
        Position west = new Position(position.getX() - 1, position.getY());

        if (biomes.containsKey(north)) {
            adjacent.put(Direction.NORTH, biomes.get(north));
        }
        if (biomes.containsKey(east)) {
            adjacent.put(Direction.EAST, biomes.get(east));
        }
        if (biomes.containsKey(south)) {
            adjacent.put(Direction.SOUTH, biomes.get(south));
        }
        if (biomes.containsKey(west)) {
            adjacent.put(Direction.WEST, biomes.get(west));
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
        return "World{" +
                "size=" + size +
                ", biomes=" + biomes.size() +
                '}';
    }
}

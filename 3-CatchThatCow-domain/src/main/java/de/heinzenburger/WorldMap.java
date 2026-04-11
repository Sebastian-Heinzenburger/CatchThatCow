package de.heinzenburger;

import java.util.*;

public class WorldMap {
    private final int size; // (2n+1)
    private final Map<Position, Biome> biomes;

    public WorldMap(int n) {
        this.size = 2 * n + 1;
        this.biomes = new HashMap<>();
        generateMap(n);
    }

    private void generateMap(int n) {
        for (int x = -n; x <= n; x++) {
            for (int y = -n; y <= n; y++) {
                Position pos = new Position(x, y);
                BiomeType type = determineBiomeType(pos);
                List<Animal> animals = createAnimalsForLevel(pos.distanceFromCenter());
                biomes.put(pos, new Biome(type, pos, animals));
            }
        }
    }

    private BiomeType determineBiomeType(Position pos) {
        if (pos.equals(new Position(0, 0))) {
            return BiomeType.START;
        }

        // Einfache Zuordnung basierend auf Position
        int x = pos.getX();
        int y = pos.getY();

        if (Math.abs(x) > Math.abs(y)) {
            return x > 0 ? BiomeType.DESERT : BiomeType.TUNDRA;
        } else {
            return y > 0 ? BiomeType.JUNGLE : BiomeType.FOREST;
        }
    }

    private List<Animal> createAnimalsForLevel(int level) {
        List<Animal> animals = new ArrayList<>();

        switch (level) {
            case 0:
            case 1:
                animals.add(createAnimal("Biene", "Bzzzzz!", 1, AnimalType.PREY, 95, 5, 2, 10));
                animals.add(createAnimal("Hase", "Hoppel!", 1, AnimalType.PREY, 85, 40, 5, 20));
                break;
            case 2:
                animals.add(createAnimal("Kobra", "Zzzzzisch!", 2, AnimalType.PREDATOR, 85, 30, 15, 70));
                animals.add(createAnimal("Wolf", "Auuuuu!", 2, AnimalType.PREDATOR, 80, 150, 50, 75));
                break;
            default:
                animals.add(createAnimal("Eisbär", "ROARRR!", 3, AnimalType.PREDATOR, 60, 250, 500, 95));
                animals.add(createAnimal("Elefant", "Töröööö!", 3, AnimalType.PREY, 40, 600, 5000, 98));
                break;
        }

        return animals;
    }

    private Animal createAnimal(String species, String noise, int level, AnimalType type,
                                int speed, int length, int weight, int strength) {
        Map<StatCategory, Integer> stats = new HashMap<>();
        stats.put(StatCategory.SPEED, speed);
        stats.put(StatCategory.LENGTH, length);
        stats.put(StatCategory.WEIGHT, weight);
        stats.put(StatCategory.STRENGTH, strength);

        return new Animal(species, noise, level, type, stats);
    }

    public Biome getBiomeAt(Position position) {
        return biomes.get(position);
    }

    public boolean isValidPosition(Position position) {
        int halfSize = size / 2;
        return Math.abs(position.getX()) <= halfSize && Math.abs(position.getY()) <= halfSize;
    }

    public List<Direction> getAvailableDirections(Position position) {
        List<Direction> directions = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Position newPos = position.move(dir);
            if (isValidPosition(newPos)) {
                directions.add(dir);
            }
        }

        return directions;
    }
}

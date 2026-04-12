package de.heinzenburger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovementOptions {

    MovementOption north;
    MovementOption east;
    MovementOption south;
    MovementOption west;

    public MovementOptions(Biome northBiome, Biome eastBiome, Biome southBiome, Biome westBiome) {
        this.north = new MovementOption(Direction.NORTH, northBiome);
        this.east = new MovementOption(Direction.EAST, eastBiome);
        this.south = new MovementOption(Direction.SOUTH, southBiome);
        this.west = new MovementOption(Direction.WEST, westBiome);
    }

    public List<MovementOption> getAvailableMovementOptions() {
        List<MovementOption> availableMovementOptions = new ArrayList<>();
        if (north.biome != null) availableMovementOptions.add(north);
        if (east.biome != null) availableMovementOptions.add(east);
        if (south.biome != null) availableMovementOptions.add(south);
        if (west.biome != null) availableMovementOptions.add(west);
        return availableMovementOptions;
    }

    public List<Direction> getAvailableDirections() {
        return getAvailableMovementOptions().stream().map(movementOption -> movementOption.direction).collect(Collectors.toList());
    }

    public static class MovementOption {
        private final Direction direction;
        private final Biome biome;

        public MovementOption(Direction direction, Biome biome) {
            this.direction = direction;
            this.biome = biome;
        }

        public Direction getDirection() {
            return direction;
        }

        public Biome getBiom() {
            return biome;
        }
    }

}

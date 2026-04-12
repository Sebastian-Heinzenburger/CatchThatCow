package de.heinzenburger;

public class Map {
    Biome[][] biomes;

    public int getBiomeCellCount() {
        return biomes.length;
    }

    /*

    mapsize: 2 -> Startingposition: (2,2)
    +---+---+---+---+---+
    |   |   |   |   |   |
    +---+---+---+---+---+
    |   |   |   |   |   |
    +---+---+---+---+---+
    |   |   | P |   |   |
    +---+---+---+---+---+
    |   |   |   |   |   |
    +---+---+---+---+---+
    |   |   |   |   |   |
    +---+---+---+---+---+

     */

    public Map(int mapsize, Position startingPosition, Random random) {
        int biomeCellCount = 2 * mapsize + 1;
        biomes = new Biome[biomeCellCount][biomeCellCount];
        initializeBiomes(biomeCellCount, startingPosition, random);
    }

    private void initializeBiomes(int biomeCellCount, Position startingPosition, Random random) {
        for (int x = 0; x < biomeCellCount; x++) {
            for (int y = 0; y < biomeCellCount; y++) {
                BiomeType biomeType = random.choose(BiomeType.class);
                int dx = Math.abs(x - startingPosition.getX());
                int dy = Math.abs(y - startingPosition.getY());
                int biomeLevel = Math.max(dx, dy);
                biomes[x][y] = new Biome(biomeType, biomeLevel);
            }
        }
    }

    public Biome getBiomeAt(Position position) {
        int x = position.getX();
        int y = position.getY();
        // return null if position is out of bounds
        if (x < 0 || x >= biomes.length || y < 0 || y >= biomes[0].length) return null;
        return biomes[x][y];
    }

    public MovementOptions getMovementOptions(Player player) {
        Position playerPosition = player.getPosition();

        Biome north = getBiomeAt(playerPosition.furtherNorth());
        Biome east = getBiomeAt(playerPosition.furtherEast());
        Biome south = getBiomeAt(playerPosition.furtherSouth());
        Biome west = getBiomeAt(playerPosition.furtherWest());

        return new MovementOptions(north, east, south, west);
    }


}
